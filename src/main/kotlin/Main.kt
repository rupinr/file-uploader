package com.freenow.fileuploader

import com.dropbox.core.DbxRequestConfig
import com.dropbox.core.v2.DbxClientV2
import org.apache.commons.cli.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*


private const val CLIENT_NAME = "CLI_CLIENT"
private const val TOKEN_LONG = "token"
private const val LOCAL_PATH_LONG = "localpath"
private const val DROPBOX_PATH_LONG = "dropboxpath"
private const val FEATURE_LONG = "feature"

private const val TOKEN_SHORT = "t"
private const val LOCAL_PATH_SHORT = "l"
private const val DROPBOX_PATH_SHORT = "d"
private const val FEATURE_SHORT = "f"

private const val NOT_EMPTY_MESSAGE = " cannot be empty"


private const val FEATURE_UPLOAD = "u"
private const val FEATURE_DOWNLOAD = "d"
private const val FEATURE_BACKUP = "b"

fun main(args: Array<String>) {

    val cmd = createCmdParser(args)
    val token = Objects.requireNonNull(cmd.getOptionValue(TOKEN_SHORT), TOKEN_LONG + NOT_EMPTY_MESSAGE)
    val feature = Objects.requireNonNull(cmd.getOptionValue(FEATURE_SHORT), FEATURE_LONG + NOT_EMPTY_MESSAGE)
    val dropboxPath = Objects.requireNonNull(cmd.getOptionValue(DROPBOX_PATH_SHORT), DROPBOX_PATH_LONG + NOT_EMPTY_MESSAGE)
    val localPath = cmd.getOptionValue(LOCAL_PATH_SHORT)

    if(feature!= FEATURE_BACKUP){
            Objects.requireNonNull(localPath, LOCAL_PATH_LONG + NOT_EMPTY_MESSAGE)
    }

    val config = DbxRequestConfig.newBuilder(CLIENT_NAME).build()
    val client = DbxClientV2(config, token)
    when (feature) {
        FEATURE_UPLOAD -> uploadFile(client, localPath, dropboxPath)
        FEATURE_DOWNLOAD -> downloadFile(client, localPath, dropboxPath)
        FEATURE_BACKUP -> backupFile(client, dropboxPath)
        else -> print("Unknown feature")
    }
}


fun createCmdParser(args: Array<String>): CommandLine {
    val options = Options()
    options.addOption(Option(TOKEN_SHORT, TOKEN_LONG, true, "Dropbox Token"))
    options.addOption(Option(LOCAL_PATH_SHORT, LOCAL_PATH_LONG, true, "Local Path"))
    options.addOption(Option(DROPBOX_PATH_SHORT, DROPBOX_PATH_LONG, true, "Dropbox Path"))
    options.addOption(Option(FEATURE_SHORT, FEATURE_LONG, true, "Upload or Download"))
    val parser: CommandLineParser = DefaultParser()
   return parser.parse(options, args)
}

fun backupFile(clientV2: DbxClientV2, dropboxPath: String){
    val metaData= clientV2.files().moveV2(dropboxPath,dropboxPath+"_BACKUP_"+System.currentTimeMillis()).metadata
    println(dropboxPath+ "moved to "+metaData.pathDisplay)
}

fun uploadFile(clientV2: DbxClientV2, localPath: String, dropBoxPath: String) {
    FileInputStream(localPath).use {
        val metadata = clientV2.files().uploadBuilder(dropBoxPath).uploadAndFinish(it)
        println(metadata.name + " Uploaded on " + metadata.clientModified)
    }
}

fun downloadFile(clientV2: DbxClientV2, localPath: String, dropboxPath: String) {
    val file = File(localPath)
    val metaData = FileOutputStream(file).use { clientV2.files().download(dropboxPath).download(it) }
    println("Downloaded file to " + localPath + " at " + metaData.clientModified)
}
