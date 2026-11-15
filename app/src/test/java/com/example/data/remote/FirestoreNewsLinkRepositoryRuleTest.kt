package com.example.data.remote

import com.example.base.FirestoreEmulatorTestBase
import com.google.firebase.firestore.FirebaseFirestoreException
import java.util.UUID
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FirestoreNewsLinkRepositoryRuleTest : FirestoreEmulatorTestBase() {

  private fun randomDocId(): String = "lnk_${UUID.randomUUID().toString().replace("-", "_")}"

  @Test
  fun upsertAndFetchOwnNewsLink_succeeds() = runBlocking {
    val uid = signInTestUser(ALICE_EMAIL)
    val repo = FirestoreNewsLinkRepository(firestore, auth)
    val docId = randomDocId()

    val upsertRes =
      withTimeout(DEFAULT_TIMEOUT_MS) {
        repo.upsertLink(
          linkId = docId,
          headline = "জাতীয় প্রেস ক্লাবে সাংবাদিক সমাবেশ",
          url = "https://bsnnews24.blogspot.com/2026/11/rally",
          normalizedKey = docId,
        )
      }
    assertTrue("Expected upsert to succeed: ${upsertRes.exceptionOrNull()}", upsertRes.isSuccess)

    val listRes = withTimeout(DEFAULT_TIMEOUT_MS) { repo.fetchAllLinksOnce(uid) }
    assertTrue(listRes.isSuccess)
    val found = listRes.getOrThrow().firstOrNull { it.id == docId }
    assertNotNull(found)
    assertEquals("জাতীয় প্রেস ক্লাবে সাংবাদিক সমাবেশ", found?.headline)
  }

  @Test
  fun observeUserLinks_emitsRealtimeUpdate_andSupportsUpdateAndDelete() = runBlocking {
    val uid = signInTestUser(ALICE_EMAIL)
    val repo = FirestoreNewsLinkRepository(firestore, auth)
    val docId = randomDocId()

    withTimeout(DEFAULT_TIMEOUT_MS) {
      repo.upsertLink(
        linkId = docId,
        headline = "প্রথম শিরোনাম",
        url = "https://bsnnews24.blogspot.com/1",
        normalizedKey = docId,
      )
    }

    val emitted =
      withTimeout(FLOW_TIMEOUT_MS) {
        repo.observeUserLinks(uid).first { list -> list.any { it.id == docId } }
      }
    assertTrue(emitted.any { it.id == docId && it.headline == "প্রথম শিরোনাম" })

    val updateRes =
      withTimeout(DEFAULT_TIMEOUT_MS) {
        repo.upsertLink(
          linkId = docId,
          headline = "পরিবর্তিত শিরোনাম",
          url = "https://bsnnews24.blogspot.com/1",
          normalizedKey = docId,
        )
      }
    assertTrue(updateRes.isSuccess)

    val deleteRes = withTimeout(DEFAULT_TIMEOUT_MS) { repo.deleteLink(docId) }
    assertTrue(deleteRes.isSuccess)
  }

  @Test
  fun crossUserRead_failsWithPermissionDenied() = runBlocking {
    val aliceUid = signInTestUser(ALICE_EMAIL)
    val aliceRepo = FirestoreNewsLinkRepository(firestore, auth)
    val docId = randomDocId()

    withTimeout(DEFAULT_TIMEOUT_MS) {
      aliceRepo.upsertLink(
        linkId = docId,
        headline = "Alice Private Link",
        url = "https://bsnnews24.blogspot.com/alice",
        normalizedKey = docId,
      )
    }

    signInTestUser(BOB_EMAIL)
    val bobRepo = FirestoreNewsLinkRepository(firestore, auth)
    val bobReadRes = withTimeout(DEFAULT_TIMEOUT_MS) { bobRepo.fetchAllLinksOnce(aliceUid) }
    assertTrue(bobReadRes.isFailure)
    val firestoreException = bobReadRes.exceptionOrNull()?.unwrapFirestoreException()
    assertNotNull(firestoreException)
    assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, firestoreException?.code)
  }

  @Test
  fun unauthenticatedObserver_failsWithPermissionDenied() = runBlocking {
    auth.signOut()
    val repo = FirestoreNewsLinkRepository(firestore, auth)

    var caught: Throwable? = null
    try {
      withTimeout(FLOW_TIMEOUT_MS) { repo.observeUserLinks("some_user_uid").first() }
    } catch (t: Throwable) {
      caught = t
    }
    assertNotNull(caught)
    val firestoreException = caught?.unwrapFirestoreException()
    assertNotNull("Expected Firestore exception, got $caught", firestoreException)
    assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, firestoreException?.code)
  }

  private companion object {
    const val ALICE_EMAIL = "alice_bsn@test.com"
    const val BOB_EMAIL = "bob_bsn@test.com"
    const val DEFAULT_TIMEOUT_MS = 5000L
    const val FLOW_TIMEOUT_MS = 4000L
  }
}
