<?php

$SERVER_BASE_URL = "http://" . $_SERVER['HTTP_HOST'] . $_SERVER['REQUEST_URI'];
$last_slash = strrpos($SERVER_BASE_URL, "/");
if($last_slash > 0)
{
	$SERVER_BASE_URL = substr($SERVER_BASE_URL, 0, $last_slash) . "/";
}

$dir = 'uploads/';
if(!file_exists($dir))
{
	mkdir($dir, 0777, true);
}
$files = scandir($dir);

$rsp_files = array();
foreach($files as $file)
{
	if($file == "." || $file == ".." || $file == "_peers.list")
	{
		continue;
	}
	
	// build meta data
	$filepath = $dir . $file;
	$url = $SERVER_BASE_URL . $filepath;
	$timestamp = filemtime($filepath);
	$md5sum = md5_file($filepath);
	$size = filesize($filepath);
	$file_obj["filename"] = $file;
	$file_obj["url"] = $url;
	$file_obj["timestamp"] = $timestamp;
	$file_obj["md5sum"] = $md5sum;
	$file_obj["size"] = $size;
	$rsp_files[] = $file_obj; // append file to list
}

// build response
$response = array(
	"count" => count($rsp_files),
	"files" => $rsp_files
);

// return JSON response
$jsonstring = json_encode($response);
$jsonstring = str_replace("\\/", "/", $jsonstring); // undo backslash escape
header('Content-Type: application/json');
echo $jsonstring;

// launch the sync.php script
exec("php -f sync.php >> /dev/null &");

?>