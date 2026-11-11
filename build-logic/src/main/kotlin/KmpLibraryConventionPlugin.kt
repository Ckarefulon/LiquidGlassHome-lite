import org.gradle.api.Plugin
import org.gradle.api.Project

/** `toolbox.kmp.library` —— 只构建 Android 一端的 KMP 库。 */
class KmpLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = target.configureKmpLibrary()
}
