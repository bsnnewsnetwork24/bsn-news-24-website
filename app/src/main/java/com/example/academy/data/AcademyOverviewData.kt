package com.example.academy.data

import com.example.academy.model.AcademicReferenceEntry
import com.example.academy.model.AcademySemester
import com.example.academy.model.AcademyYearInfo

object AcademyOverviewData {
  const val ACADEMY_TITLE_EN = "BSN JOURNALISM ACADEMY"
  const val ACADEMY_TITLE_BILINGUAL = "Mass Communication & Journalism / গণযোগাযোগ ও সাংবাদিকতা"
  const val ACADEMY_SUBTITLE_EN = "Four-Year Professional Journalism Education Programme"
  const val ACADEMY_SUBTITLE_BN = "চার বছর মেয়াদি পূর্ণাঙ্গ সাংবাদিকতা শিক্ষা কার্যক্রম।"

  const val INDEPENDENT_PROGRAM_NOTICE_BN =
    "গুরুত্বপূর্ণ একাডেমিক সততা ঘোষণা: BSN Journalism Academy একটি স্বাধীন পেশাগত ও শিক্ষামূলক প্রোগ্রাম। এটি কোনো বিশ্ববিদ্যালয় বা সরকারি বোর্ড কর্তৃক অনুমোদিত, স্বীকৃত বা প্রদত্ত একাডেমিক ডিগ্রি প্রোগ্রাম নয়। আন্তর্জাতিক ও বাংলাদেশের খ্যাতনামা সাংবাদিকতা শিক্ষা প্রতিষ্ঠানের প্রকাশিত পাঠ্যক্রম কাঠামো ও UNESCO সাংবাদিকতা শিক্ষা নির্দেশিকা থেকে অনুপ্রাণিত হয়ে সাধারণ শিক্ষার্থীদের জন্য সহজ বাংলায় নিজস্ব BSN পাঠ্যক্রম হিসেবে এটি তৈরি করা হয়েছে।"

  val years: List<AcademyYearInfo> =
    listOf(
      AcademyYearInfo(
        year = 1,
        titleEn = "YEAR 1 — JOURNALISM FOUNDATION",
        titleBn = "প্রথম বর্ষ — সাংবাদিকতার মৌলিক ভিত্তি",
        overviewBn =
          "যোগাযোগের মূল তত্ত্ব, সংবাদের সংজ্ঞা ও মূল্যমান, বাংলা-ইংরেজি সংবাদ ভাষা, প্রিন্ট মিডিয়া, সংবাদ সংগ্রহ, প্রাথমিক রিপোর্টিং, বার্তাকক্ষ পরিচালনা এবং মিডিয়া সাক্ষরতা গড়ে তোলার ভিত্তি বছর।",
      ),
      AcademyYearInfo(
        year = 2,
        titleEn = "YEAR 2 — PROFESSIONAL REPORTING",
        titleBn = "দ্বিতীয় বর্ষ — পেশাদার রিপোর্টিং ও সম্পাদনা",
        overviewBn =
          "উন্নত রিপোর্টিং, সাব-এডিটিং, শিরোনাম রচনা, ফিচার লেখা, সাক্ষাৎকার কৌশল, অনুসন্ধানী ও বিট ভিত্তিক সাংবাদিকতা (রাজনীতি, অপরাধ, আদালত, অর্থনীতি, উন্নয়ন, পরিবেশ, স্বাস্থ্য, শিক্ষা) এবং ফ্যাক্ট-চেকিং দক্ষতা অর্জন।",
      ),
      AcademyYearInfo(
        year = 3,
        titleEn = "YEAR 3 — MULTIMEDIA & DIGITAL JOURNALISM",
        titleBn = "তৃতীয় বর্ষ — মাল্টিমিডিয়া ও ডিজিটাল সাংবাদিকতা",
        overviewBn =
          "টেলিভিশন ও রেডিও ব্রডকাস্ট, নিউজ প্রেজেন্টেশন, মোবাইল জার্নালিজম (MoJo), ফটো ও ভিডিও জার্নালিজম, ডেটা জার্নালিজম, OSINT ও ডিজিটাল যাচাইকরণ, পডকাস্ট, ডকুমেন্টারি এবং সরাসরি সম্প্রচার (Live Reporting)।",
      ),
      AcademyYearInfo(
        year = 4,
        titleEn = "YEAR 4 — ADVANCED & INTERNATIONAL JOURNALISM",
        titleBn = "চতুর্থ বর্ষ — উচ্চতর, আইনগত ও আন্তর্জাতিক সাংবাদিকতা",
        overviewBn =
          "আন্তর্জাতিক সংবাদ ব্যবস্থা, গণমাধ্যম আইন ও নৈতিকতা, সংবাদপত্রের স্বাধীনতা, মানবাধিকার ও সংকটকালীন রিপোর্টিং, সাংবাদিকতায় কৃত্রিম বুদ্ধিমত্তা (AI), মিডিয়া উদ্যোক্তা, নিউজরুম নেতৃত্ব, ইন্টার্নশিপ, পোর্টফোলিও এবং চূড়ান্ত প্রকল্প।",
      ),
    )

  val semesters: List<AcademySemester> =
    listOf(
      AcademySemester(
        year = 1,
        semesterNumber = 1,
        titleEn = "Semester 1: Communication & News Core",
        titleBn = "সেমিস্টার ১: যোগাযোগ ও সংবাদের মৌলিক ধারণা",
        goalBn = "গণযোগাযোগ, সাংবাদিকতার ইতিহাস, যোগাযোগ তত্ত্ব, সংবাদের মূল্যমান ও মিডিয়া সাক্ষরতা বোঝা।",
      ),
      AcademySemester(
        year = 1,
        semesterNumber = 2,
        titleEn = "Semester 2: Reporting Language & Newsroom Base",
        titleBn = "সেমিস্টার ২: সংবাদ ভাষা, সংগ্রহ ও বার্তাকক্ষ ভিত্তি",
        goalBn = "সংবাদ সংগ্রহ, উল্টো পিরামিড কাঠামোয় সংবাদ লেখা, সাংবাদিকতার বাংলা ও ইংরেজি, প্রিন্ট এবং গবেষণা পদ্ধতি আয়ত্ত করা।",
      ),
      AcademySemester(
        year = 2,
        semesterNumber = 3,
        titleEn = "Semester 3: Editing, Feature & Verification",
        titleBn = "সেমিস্টার ৩: সম্পাদনা, ফিচার ও যাচাইকরণ",
        goalBn = "উন্নত রিপোর্টিং, সাব-এডিটিং, শিরোনাম, ফিচার, সাক্ষাৎকার কৌশল, ফ্যাক্ট-চেকিং ও সোর্স যাচাই শেখা।",
      ),
      AcademySemester(
        year = 2,
        semesterNumber = 4,
        titleEn = "Semester 4: Specialized Beat & Investigative Reporting",
        titleBn = "সেমিস্টার ৪: বিট ভিত্তিক ও অনুসন্ধানী সাংবাদিকতা",
        goalBn = "রাজনৈতিক, অপরাধ, আদালত, অর্থনীতি, উন্নয়ন, পরিবেশ, স্বাস্থ্য, শিক্ষা রিপোর্টিং এবং নিউজরুম ব্যবস্থাপনা শেখা।",
      ),
      AcademySemester(
        year = 3,
        semesterNumber = 5,
        titleEn = "Semester 5: Broadcast, Video & MoJo Production",
        titleBn = "সেমিস্টার ৫: টেলিভিশন, রেডিও ও মোবাইল জার্নালিজম",
        goalBn = "টিভি-রেডিও স্ক্রিপ্ট ও প্রোডাকশন, সংবাদ উপস্থাপনা, ভিডিও ও ফটোজার্নালিজম এবং মোবাইল জার্নালিজম অনুশীলন।",
      ),
      AcademySemester(
        year = 3,
        semesterNumber = 6,
        titleEn = "Semester 6: Digital, Data, OSINT & Live Journalism",
        titleBn = "সেমিস্টার ৬: ডিজিটাল, ডেটা, OSINT ও লাইভ রিপোর্টিং",
        goalBn = "অনলাইন প্রকাশনা, সোশ্যাল মিডিয়া, ডেটা সাংবাদিকতা, ডিজিটাল ভেরিফিকেশন, পডকাস্ট, ডকুমেন্টারি ও লাইভ রিপোর্টিং দক্ষতা।",
      ),
      AcademySemester(
        year = 4,
        semesterNumber = 7,
        titleEn = "Semester 7: Global Systems, Law, Ethics & AI",
        titleBn = "সেমিস্টার ৭: আন্তর্জাতিক গণমাধ্যম, আইন, নৈতিকতা ও AI",
        goalBn = "বৈশ্বিক সংবাদ ব্যবস্থা, মিডিয়া আইন, নৈতিকতা, মানবাধিকার, সংঘাত-পরিবেশ-বিজ্ঞান রিপোর্টিং এবং AI-ভিত্তিক সাংবাদিকতা বোঝা।",
      ),
      AcademySemester(
        year = 4,
        semesterNumber = 8,
        titleEn = "Semester 8: Leadership, Research, Internship & Capstone",
        titleBn = "সেমিস্টার ৮: নেতৃত্ব, গবেষণা, ইন্টার্নশিপ ও চূড়ান্ত প্রকল্প",
        goalBn = "মিডিয়া উদ্যোক্তা, নিউজরুম নেতৃত্ব, উচ্চতর গবেষণা, পেশাদার পোর্টফোলিও, ইন্টার্নশিপ, চূড়ান্ত প্রজেক্ট ও ক্যারিয়ার গঠন।",
      ),
    )

  val academicReferences: List<AcademicReferenceEntry> =
    listOf(
      AcademicReferenceEntry(
        categoryBn = "প্রকাশিত বিশ্ববিদ্যালয় পাঠ্যক্রম ও একাডেমিক কাঠামো (Published Syllabus Context)",
        title = "University of Dhaka — Department of Mass Communication & Journalism (BSS Honours Framework)",
        detailsBn =
          "বাংলাদেশের প্রেক্ষাপটে ৪-বছর/৮-সেমিস্টার ভিত্তিক গণযোগাযোগ, বাংলাদেশের গণমাধ্যম ইতিহাস, উন্নয়ন সাংবাদিকতা, মিডিয়া আইন ও গবেষণা কাঠামো বুঝতে বিষয় বিন্যাস রেফারেন্স হিসেবে পর্যালোচনা করা হয়েছে। এটি ঢাকা বিশ্ববিদ্যালয়ের কোনো অনুমোদিত বা অনুকৃত প্রোগ্রাম নয়।",
      ),
      AcademicReferenceEntry(
        categoryBn = "আন্তর্জাতিক সাংবাদিকতা স্কুলের প্রকাশিত কাঠামো (US / UK / Australia Benchmarks)",
        title = "Columbia Journalism School, Missouri School of Journalism, Cardiff University (JOMEC), University of Queensland",
        detailsBn =
          "প্রকাশিত কোর্স ক্যাটালগে উল্লিখিত মূল দক্ষতা—যেমন Reporting, Verification, Investigative Methods, Broadcast & Digital Storytelling, Data Journalism, Ethics এবং Capstone Portfolio কাঠামো থেকে শিক্ষাক্রম অনুপ্রাণিত।",
      ),
      AcademicReferenceEntry(
        categoryBn = "আন্তর্জাতিক সংস্থা ও উন্মুক্ত শিক্ষামূলক নির্দেশিকা (Open Educational Resources)",
        title = "UNESCO Model Curricula for Journalism Education & UNESCO Handbook for Journalism Educators (Verification / Fake News)",
        detailsBn =
          "UNESCO-এর উন্মুক্ত সাংবাদিকতা শিক্ষা পাঠ্যক্রম ও তথ্য-যাচাই নির্দেশিকার মূল ধারণা অবলম্বনে যাচাইকরণ, জনস্বার্থ সাংবাদিকতা, নৈতিকতা ও নিরাপত্তাবিষয়ক পাঠ তৈরি করা হয়েছে।",
      ),
      AcademicReferenceEntry(
        categoryBn = "মৌলিক BSN পাঠ ও অনুশীলন (Original BSN Content)",
        title = "BSN Journalism Academy Original Bangla Lessons, Glossary & Exercises",
        detailsBn =
          "অ্যাপের প্রতিটি অধ্যায়ের বাংলা ব্যাখ্যা, ইংরেজি পরিভাষার অর্থ, সংজ্ঞা, ব্যবহারিক অনুশীলন ও অ্যাসাইনমেন্ট নিজস্বভাবে BSN শিক্ষার্থীদের উপযোগী করে রচিত। কোনো কপিরাইটযুক্ত পূর্ণাঙ্গ বই বা পেইড কোর্সপ্যাক কপি করা হয়নি।",
      ),
      AcademicReferenceEntry(
        categoryBn = "মূল্যায়ন ও প্রশ্নপত্র (BSN Model Questions)",
        title = "BSN Model Questions / BSN নমুনা প্রশ্ন",
        detailsBn =
          "সকল অধ্যায়ের কুইজ, সংক্ষিপ্ত প্রশ্ন, রচনামূলক প্রশ্ন ও MCQ সম্পূর্ণ অনুশীলনমূলক 'BSN Model Questions / BSN নমুনা প্রশ্ন'—এগুলো কোনো বিশ্ববিদ্যালয়ের প্রকৃত বিগত পরীক্ষার প্রশ্নপত্র নয়।",
      ),
      AcademicReferenceEntry(
        categoryBn = "সুপারিশকৃত বাহ্যিক পাঠ্য তালিকা (Recommended External Reading)",
        title = "UNESCO Series on Journalism Education; Reuters Institute Digital News Reports; Press Institute Bangladesh (PIB) Manuals; Poynter Institute Guides",
        detailsBn =
          "শিক্ষার্থীরা নিজেদের উদ্যোগে বাংলাদেশ প্রেস ইনস্টিটিউট (PIB), UNESCO ওয়াল্ড ওয়েবসাইট, Poynter, এবং Reuters Institute-এর উন্মুক্ত প্রতিবেদন পড়ে বাড়তি জ্ঞান অর্জন করতে পারবেন।",
      ),
    )
}
