
import groovy.util.Node
import groovy.util.NodeList
import org.jetbrains.kotlin.konan.target.buildDistribution
import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    `maven-publish`
}



val projectProps = Properties()
projectProps.load(FileInputStream(project.file("project.properties")))

val projectName = projectProps.getProperty("name")
val projectTitle = projectProps.getProperty("title")
val projectVersion = projectProps.getProperty("version")
val projectGroupId = projectProps.getProperty("publication_group_id")
val projectArtifactId = projectProps.getProperty("publication_artifact_id")

val githubUrl = projectProps.getProperty("github_url")
val githubUsername = projectProps.getProperty("github_user_name")
val githubAccessToken = projectProps.getProperty("github_access_token")

android {
    namespace = "kr.co.hconnect.bluetoothlib"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                groupId = projectGroupId
                artifactId = projectArtifactId
                version = projectVersion
                pom.packaging = "aar"
                artifact("$buildDir/outputs/aar/bluetoothlib-release.aar")

                pom.withXml {
                    val dependenciesNode = asNode().appendNode("dependencies")
                    configurations.implementation.get().allDependencies.forEach { dependency ->
                        if (!dependency.group!!.startsWith("androidx")) {
                            val dependencyNode = dependenciesNode.appendNode("dependency")
                            dependencyNode.appendNode("groupId", dependency.group)
                            dependencyNode.appendNode("artifactId", dependency.name)
                            dependencyNode.appendNode("version", dependency.version)
                        }
                    }
                }
            }
        }

        repositories {
            maven {
                name = "GitHubPackages"
                url = uri(githubUrl)
                credentials {
                    username = githubUsername
                    password = githubAccessToken
                }
            }
        }
    }
}

tasks.register("publishBuildArtifacts") {
    group = "publishing"
    description = "Publishes the release build to GitHub Packages"
    dependsOn("publish")
}