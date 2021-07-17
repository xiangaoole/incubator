# incubator

## 接入 React Native

当前版本同时接入了 React Native。注意以下依赖 node_module 的位置：

```groovy
// [settings.gradle]
apply from: file("../node_modules/@react-native-community/cli-platform-android/native_modules.gradle"); applyNativeModulesSettingsGradle(settings)

// [app/build.gradle']
apply from: file("../../node_modules/@react-native-community/cli-platform-android/native_modules.gradle"); applyNativeModulesAppBuildGradle(project)

// [build.gradle]
allprojects {
    repositories {
        maven {
            // All of React Native (JS, Android binaries) is installed from npm
            url "$rootDir/../node_modules/react-native/android"
        }
        maven {
            // Android JSC is installed from npm
            url("$rootDir/../node_modules/jsc-android/dist")
        }
        ...
    }
    ...
}
```

当前没有做到完全解耦，Android 项目依赖上一层 RN 目录中的库和脚本，需要先拉取 RN 项目的代码。运行 `yarn install` 构建。