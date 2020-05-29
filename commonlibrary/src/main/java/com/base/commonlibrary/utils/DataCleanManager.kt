package com.base.commonlibrary.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import autodispose2.AutoDispose
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider
import com.blankj.utilcode.util.CleanUtils
import com.blankj.utilcode.util.ToastUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File
import java.math.BigDecimal

/**
 * 数据删除工具类
 *
 */
object DataCleanManager {
    /**
     * 清除本应用内部缓存
     * (/data/data/com.xxx.xxx/cache)
     *
     * @param context
     */
    fun cleanInternalCache(context: Context) {
        deleteFilesByDirectory(context.cacheDir)
        deleteFilesByDirectory(context.filesDir)
    }

    /**
     * 清楚本应用所有数据库
     * (/data/data/com.xxx.xxx/databases)
     *
     * @param context
     */
    @SuppressLint("SdCardPath")
    fun cleanDatabases(context: Context) {
        deleteFilesByDirectory(
            File(
                "/data/data/"
                        + context.packageName + "/databases"
            )
        )
    }

    /**
     * 清除本应用SharedPreference
     * (/data/data/com.xxx.xxx/shared_prefs)
     *
     * @param context
     */
    @SuppressLint("SdCardPath")
    fun cleanSharedPreference(context: Context) {
        deleteFilesByDirectory(
            File(
                "/data/data/"
                        + context.packageName + "/shared_prefs"
            )
        )
    }

    /**
     * 按名字清除本应用数据库
     *
     * @param context
     * @param dbName
     */
    fun cleanDatabaseByName(context: Context, dbName: String?) {
        context.deleteDatabase(dbName)
    }

    /**
     * 清除/data/data/com.xxx.xxx/files下的内容
     *
     * @param context
     */
    fun cleanFiles(context: Context) {
        deleteFilesByDirectory(context.filesDir)
    }

    /**
     * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
     *
     * @param context
     */
    fun cleanExternalCache(context: Context) {
        if (Environment.getExternalStorageState() ==
            Environment.MEDIA_MOUNTED
        ) {
            deleteFilesByDirectory(context.externalCacheDir)
        }
    }

    /**
     * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除
     *
     * @param filePath
     */
    fun cleanCustomCache(filePath: String?) {
        deleteFilesByDirectory(File(filePath))
    }

    /**
     * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除
     *
     * @param file
     */
    fun cleanCustomCache(file: File?) {
        deleteFilesByDirectory(file)
    }

    /**
     * 清除本应用所有的数据
     *
     * @param context
     * @param filepath
     */
    fun cleanApplicationData(
        context: Context,
        vararg filepath: String?
    ) {
        cleanInternalCache(context)
        cleanExternalCache(context)
        cleanDatabases(context)
        cleanSharedPreference(context)
        cleanFiles(context)
        for (filePath in filepath) {
            cleanCustomCache(filePath)
        }
    }

    /**
     * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理
     *
     * @param directory
     */
    private fun deleteFilesByDirectory(directory: File?) {
        if (directory != null && directory.exists() && directory.isDirectory) {
            for (child in directory.listFiles()) {
                if (child.isDirectory) {
                    deleteFilesByDirectory(child)
                }
                child.delete()
            }
        }
    }

    /**
     * 获取缓存大小
     *
     * @param context
     * @return
     * @throws Exception
     */
    fun getTotalCacheSize(context: Context): String {
        var totalCacheSize = "0K"
        try {
            var cacheSize = getFolderSize(context.cacheDir)
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                cacheSize += getFolderSize(context.externalCacheDir)
            }
            totalCacheSize = getFormatSize(cacheSize.toDouble())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return totalCacheSize
    }

    /**
     * 清除缓存
     *
     * @param context
     */
    fun clearAllCache(context: Context) {
        deleteDir(context.cacheDir)
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            deleteDir(context.externalCacheDir)
        }
    }

    private fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success =
                    deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }
        return dir!!.delete()
    }

    // 获取文件大小
    //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
    //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    @Throws(Exception::class)
    fun getFolderSize(file: File?): Long {
        var size: Long = 0
        try {
            val fileList = file!!.listFiles()
            for (i in fileList.indices) {
                // 如果下面还有文件
                size = if (fileList[i].isDirectory) {
                    size + getFolderSize(fileList[i])
                } else {
                    size + fileList[i].length()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return size
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    fun getFormatSize(size: Double): String {
        val kiloByte = size / 1024
        if (kiloByte < 1) {
//            return size + "Byte";
            return "0K"
        }
        val megaByte = kiloByte / 1024
        if (megaByte < 1) {
            val result1 =
                BigDecimal(java.lang.Double.toString(kiloByte))
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "K"
        }
        val gigaByte = megaByte / 1024
        if (gigaByte < 1) {
            val result2 =
                BigDecimal(java.lang.Double.toString(megaByte))
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "M"
        }
        val teraBytes = gigaByte / 1024
        if (teraBytes < 1) {
            val result3 =
                BigDecimal(java.lang.Double.toString(gigaByte))
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "GB"
        }
        val result4 = BigDecimal(teraBytes)
        return (result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB")
    }

    /**
     * 清除缓存（RxJava2）
     *
     * @param owner LifecycleOwner
     */
    fun cleanCache(owner: LifecycleOwner?) {
        // 清理内部缓存
        val obs1 =
            Observable
                .create<Boolean> { emitter -> emitter.onNext(CleanUtils.cleanInternalCache()) }
        // 清理外部缓存
        val obs2 =
            Observable
                .create<Boolean> { emitter -> emitter.onNext(CleanUtils.cleanExternalCache()) }
        obs1.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { aBoolean ->
                if (!aBoolean!!) {
                     ToastUtils.showShort("清除缓存失败");
                }
            }
            .filter { aBoolean -> aBoolean }
            .observeOn(Schedulers.io())
            .flatMap { obs2 }
            .observeOn(AndroidSchedulers.mainThread())
            .to(
                AutoDispose.autoDisposable(
                    AndroidLifecycleScopeProvider
                        .from(owner, Lifecycle.Event.ON_DESTROY)
                )
            )
            .subscribe({ ToastUtils.showShort("清除成功") }) {
                 ToastUtils.showShort("清除缓存失败");
            }
    }
}