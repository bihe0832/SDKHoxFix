package com.bihe0832.util

class FileUtils {

    public static File touchFile(File dir, String path) {
        def file = new File("${dir}/${path}")
        file.getParentFile().mkdirs()
        return file
    }

    public static copyBytesToFile(byte[] bytes, File file) {
        if (!file.exists()) {
            file.createNewFile()
        }
        org.apache.commons.io.FileUtils.writeByteArrayToFile(file, bytes)
    }

}
