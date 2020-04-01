# file-uploader
Dropbox file uploader

## Options
```bash
-f , --feature u (Upload) , d (Download), b (Backup)
-t, --token DropBox token
-d, --dropboxpath Path to file in dropbox
-l, --localpath Local file path

```

## To Download

```bash
java -jar build/libs/file-uploader-1.0.jar -t {{DROPBOX_TOKEN}} -d /DropBox/Path/To/File.text  -l /Local/Path/File.text -f u
```

## To Upload
```bash
java -jar build/libs/file-uploader-1.0.jar -t {{DROPBOX_TOKEN}} -d /DropBox/Path/To/File.text  -l /Local/Path/File.text -f u
````

## To Backup
```
java -jar build/libs/file-uploader-1.0.jar -t {{DROPBOX_TOKEN}} -d /DropBox/Path/To/File.text -f b

```
