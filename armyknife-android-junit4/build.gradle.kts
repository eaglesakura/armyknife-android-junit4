apply(from = "../dsl/android-library.gradle")
apply(from = "../dsl/ktlint.gradle")
apply(from = "../dsl/bintray.gradle")

dependencies {
    /**
     * Support Libraries
     * https://developer.android.com/topic/libraries/architecture/adding-components
     * https://developer.android.com/topic/libraries/support-library/refactor
     */
    "api"("androidx.activity:activity:1.1.0-rc01")
    "api"("androidx.activity:activity-ktx:1.1.0-rc01")
    "api"("androidx.core:core:1.1.0")
    "api"("androidx.core:core-ktx:1.1.0")
    "api"("androidx.collection:collection:1.1.0")
    "api"("androidx.collection:collection-ktx:1.1.0")
    "api"("androidx.fragment:fragment:1.1.0")
    "api"("androidx.fragment:fragment-ktx:1.1.0")
    "api"("androidx.appcompat:appcompat:1.1.0")
    "api"("androidx.appcompat:appcompat-resources:1.1.0")
    "api"("androidx.lifecycle:lifecycle-extensions:2.1.0")
    "api"("androidx.lifecycle:lifecycle-viewmodel:2.1.0")
    "api"("androidx.lifecycle:lifecycle-viewmodel-savedstate:1.0.0-rc01")
    "api"("androidx.lifecycle:lifecycle-viewmodel-ktx:2.1.0")
    "api"("androidx.lifecycle:lifecycle-runtime:2.1.0")
    "api"("androidx.lifecycle:lifecycle-common-java8:2.1.0")
    "api"("androidx.lifecycle:lifecycle-reactivestreams:2.1.0")
    "api"("androidx.lifecycle:lifecycle-reactivestreams-ktx:2.1.0")

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