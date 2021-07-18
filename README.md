# incubator

本项目目前作为上层 [RnHybridApp](https://github.com/xiangaoole/RnHybridApp) 项目的子模块，请先克隆上层项目，按照上层项目的 README 进行操作。

在父项目中，运行 `yarn install` 构建，再用 Android Studio 打开 android 目录，就可以正常编译运行了。

如果你希望更简单的方式解决 RN 依赖，可以只拷贝父项目的 package.json 文件到上层目录，并将本项目目录名改为 android，在 package.json 目录中运行 `yarn install` 构建，生成 node_modules 文件夹即可。

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

要编译生成最新的 index.android.bundle 包，请在父项目的 package.json 所在目录中，运行：

```bash
react-native bundle --platform android --dev false --entry-file index.js --bundle-output android/app/src/main/assets/index.android.bundle --assets-dest android/app/src/main/res
```