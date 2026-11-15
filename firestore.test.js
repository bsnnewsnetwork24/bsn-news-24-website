const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_uid_123";
const BOB_UID = "bob_uid_456";

const [emulatorHost, emulatorPortStr] = (process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085").split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

function validLinkPayload(uid, overrides = {}) {
  const pastTime = new Date(Date.now() - 2000);
  return {
    userId: uid,
    headline: "ঢাকায় সাংবাদিকদের বিশেষ সম্মেলন অনুষ্ঠিত",
    url: "https://bsnnews24.blogspot.com/2026/11/conference",
    normalizedKey: "link_key_001",
    createdAt: pastTime,
    updatedAt: pastTime,
    ...overrides,
  };
}

test("Unauthenticated user cannot read or write news_links", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).collection("news_links").get());
  await assertFails(
    unauthDb.collection("users").doc(ALICE_UID).collection("news_links").doc("link_1").set(validLinkPayload(ALICE_UID))
  );
});

test("Authenticated owner can create, list, get, update, and delete own news link", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const docRef = aliceDb.collection("users").doc(ALICE_UID).collection("news_links").doc("link_1");

  await assertSucceeds(docRef.set(validLinkPayload(ALICE_UID)));
  await assertSucceeds(docRef.get());
  await assertSucceeds(aliceDb.collection("users").doc(ALICE_UID).collection("news_links").get());
  await assertSucceeds(
    docRef.update({
      headline: "আপডেটেড শিরোনাম",
      updatedAt: new Date(Date.now() - 1000),
    })
  );
  await assertSucceeds(docRef.delete());
});

test("Cross-user access is strictly denied", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();

  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("news_links").doc("link_alice").set(validLinkPayload(ALICE_UID))
  );

  await assertFails(bobDb.collection("users").doc(ALICE_UID).collection("news_links").get());
  await assertFails(bobDb.collection("users").doc(ALICE_UID).collection("news_links").doc("link_alice").get());
  await assertFails(
    bobDb.collection("users").doc(ALICE_UID).collection("news_links").doc("link_bob").set(validLinkPayload(BOB_UID))
  );
  await assertFails(
    bobDb.collection("users").doc(ALICE_UID).collection("news_links").doc("link_alice").update({
      headline: "Hacked",
      updatedAt: new Date(Date.now() - 1000),
    })
  );
  await assertFails(bobDb.collection("users").doc(ALICE_UID).collection("news_links").doc("link_alice").delete());
});

test("Identity spoofing and shadow fields are rejected", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const docRef = aliceDb.collection("users").doc(ALICE_UID).collection("news_links").doc("link_bad");

  await assertFails(docRef.set(validLinkPayload(BOB_UID)));
  await assertFails(docRef.set(validLinkPayload(ALICE_UID, { isAdmin: true })));
  await assertFails(docRef.set(validLinkPayload(ALICE_UID, { headline: "" })));
});

test("Invalid document IDs and createdAt tampering on update are rejected", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertFails(
    aliceDb.collection("users").doc(ALICE_UID).collection("news_links").doc("bad.id!").set(validLinkPayload(ALICE_UID))
  );

  const validRef = aliceDb.collection("users").doc(ALICE_UID).collection("news_links").doc("link_immutable");
  await assertSucceeds(validRef.set(validLinkPayload(ALICE_UID)));
  await assertFails(
    validRef.update({
      createdAt: new Date(Date.now() - 500),
      updatedAt: new Date(Date.now() - 500),
    })
  );
});
