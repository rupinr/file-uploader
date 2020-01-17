package com.freenow.fileuploader

import com.dropbox.core.DbxRequestConfig
import com.dropbox.core.v2.DbxClientV2
import org.apache.commons.cli.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*


private const val CLIENT_NAME = "FareScraperApk"
private const val TOKEN = "TOKEN"
private const val LOCAL_PATH = "LOCAL_PATH"
private const val DROPBOX_PATH = "DROPBOX_PATH"

private const val NOT_EMPTY_MESSAGE = " cannot be empty"
private const val FEATURE = "FEATURE"
private const val UPLOAD = "u"
private const val DOWNLOAD = "d"


fun main(args: Array<String>) {


    val options = Options()
    options.addOption(Option("t", "token", true, "Dropbox Token"))
    options.addOption(Option("l", "localpath", true, "Local Path"))
    options.addOption(Option("d", "dropboxpath", true, "Dropbox Path"))
    options.addOption(Option("f", "feature", true, "Upload or Download"))
    val parser: CommandLineParser = DefaultParser()
    val cmd: CommandLine = parser.parse(options, args)


    val token = Objects.requireNonNull(cmd.getOptionValue("t"), TOKEN + NOT_EMPTY_MESSAGE)
    val localPath = Objects.requireNonNull(cmd.getOptionValue("l"), LOCAL_PATH + NOT_EMPTY_MESSAGE)
    val dropboxPath = Objects.requireNonNull(cmd.getOptionValue("d"), DROPBOX_PATH + NOT_EMPTY_MESSAGE)
    val feature = Objects.requireNonNull(cmd.getOptionValue("f"), FEATURE + NOT_EMPTY_MESSAGE)

    val config = DbxRequestConfig.newBuilder(CLIENT_NAME).build()
    val client = DbxClientV2(config, token)
    when (feature) {
        UPLOAD -> {
            uploadFile(client, localPath, dropboxPath)
        }
        DOWNLOAD -> downloadFile(client, localPath, dropboxPath)
        else -> print("Unknown feature")
    }
}


fun uploadFile(clientV2: DbxClientV2, localPath: String, dropBoxPath: String) {
    FileInputStream(localPath).use {
        val metadata = clientV2.files().uploadBuilder(dropBoxPath).uploadAndFinish(it)
        print(metadata.name + " Uploaded on " + metadata.clientModified)
    }
}

fun downloadFile(clientV2: DbxClientV2, localPath: String, dropboxPath: String) {
    val file = File(localPath)
    val metaData = FileOutputStream(file).use { clientV2.files().download(dropboxPath).download(it) }
    print("Downloaded file to " + localPath + " at " + metaData.clientModified)
}