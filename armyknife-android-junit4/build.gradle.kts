apply(from = "../dsl/android-library.gradle")

dependencies {
    /**
     * Support Libraries
     * https://developer.android.com/topic/libraries/architecture/adding-components
     * https://developer.android.com/topic/libraries/support-library/refactor
     */
    "implementation"("androidx.annotation:annotation:1.0.2")
    "compileOnly"("androidx.core:core-ktx:1.0.2")
    "compileOnly"("androidx.collection:collection-ktx:1.0.0")
    "compileOnly"("androidx.fragment:fragment-ktx:1.0.0")
    "compileOnly"("androidx.appcompat:appcompat:1.0.2")

    /**
     * Test Tools.
     */
    "api"("org.assertj:assertj-core:3.12.1")
    "api"("junit:junit:4.12")
    "api"("androidx.test:core:1.2.0")
    "api"("androidx.test:monitor:1.2.0")
    "api"("androidx.test.ext:junit:1.1.1")
    "api"("androidx.test:rules:1.2.0")
    "api"("com.nhaarman:mockito-kotlin:1.6.0")
    "api"("com.nhaarman:mockito-kotlin-kt1.1:1.6.0")
    "api"("androidx.test.espresso:espresso-core:3.2.0")
    "compileOnly"("org.robolectric:robolectric:4.2.1") {
        exclude(group = "com.google.code.findbugs")
    }
}