# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html.
# For more examples on how to use CMake, see https://github.com/android/ndk-samples.

# Sets the minimum CMake version required for this project.
cmake_minimum_required(VERSION 3.18.1)

# Declares the project name. The project name can be accessed via ${ PROJECT_NAME},
# Since this is the top level CMakeLists.txt, the project name is also accessible
# with ${CMAKE_PROJECT_NAME} (both CMake variables are in-sync within the top level
# build script scope).
project("keep_alive")

include_directories(${CMAKE_CURRENT_SOURCE_DIR}/)

add_compile_options(-fno-rtti -O3)

message(@@@${CMAKE_CURRENT_SOURCE_DIR})

#link_directories(binder_libs/${CMAKE_ANDROID_ARCH_ABI})

aux_source_directory(. SRC_LIST) # 搜索当前目录下的所有.cpp文件
aux_source_directory(./utils UTIL_SRC_LIST)

# open follow annotate, if you want to debug the code.
# add_definitions(-DLIB_DEBUG)

add_library( # Sets the name of the library.
        keep_alive

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        ${SRC_LIST} ${UTIL_SRC_LIST})

find_library(log-lib log)

target_link_libraries(
        keep_alive
        ${log-lib})