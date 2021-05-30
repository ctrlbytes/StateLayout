# CodeKit

[![](https://jitpack.io/v/ctrlbytes/StateLayout.svg)](https://jitpack.io/#ctrlbytes/StateLayout)
[![API](https://img.shields.io/badge/API-19%2B-orange.svg?style=flat)](https://android-arsenal.com/api?level=19)
![Android CI](https://github.com/ctrlbytes/StateLayout/actions/workflows/android.yml/badge.svg)

## Adding to your project

1. Add the JitPack repository to your build.gradle at the end of repositories:

```groovy
repositories {
  ...
  maven { url 'https://jitpack.io' }
}
```

2. Add the dependency

```groovy
implementation 'com.github.ctrlbytes:StateLayout:LATEST_VERSION_HERE'
```

## Usage
```xml
<com.ctrlbytes.statelayout.StateLayout
    android:id="@+id/state_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:buttonText="@string/try_again"
    app:emptyImage="@drawable/ic_inbox"
    app:errorImage="@drawable/ic_cactus">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:text="Inbox Content" />

</com.ctrlbytes.statelayout.StateLayout>
```

```kotlin
showContent()
showLoading()
showError(message)
showEmpty(message)

onButtonClick {
    // handle button click
}
```

## Attributes

| Attribute  | Usage             |
|------------|-------------------|
| buttonText | Retry Button Text |
| emptyImage | Empty State Icon  |
| errorImage | Error State Icon  |