pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Potok"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":app")
include(":core:analytics")
include(":core:common")
include(":core:data")
include(":core:database")
//include(":core:datastore")
//include(":core:datastore-proto")
//include(":core:datastore-test")
include(":core:designsystem")
include(":core:model")
include(":core:network")
include(":core:notifications")
//include(":core:screenshot-testing")
//include(":core:testing")
include(":core:ui")

include(":feature:lifearea")
include(":feature:task")
