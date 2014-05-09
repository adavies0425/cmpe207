<?php

/** Accounts for symbols and spaces in the file name of the URL. **/
function file_url($url){
  $parts = parse_url($url);
  $path_parts = array_map('rawurldecode', explode('/', $parts['path']));

  return
    $parts['scheme'] . '://' .
    $parts['host'] .
    implode('/', array_map('rawurlencode', $path_parts))
  ;
}

// iterate through _peers.list and synchronize with each one
$peers_string = file_get_contents("_peers.json");
$peers = json_decode($peers_string, true);
foreach($peers as $peer)
{
	$url = $peer["list"];
	echo "Getting peer list: " . $url . "\n";
	$rsp_string = file_get_contents(file_url($url));
	$rsp = json_decode($rsp_string, true);
	$files = $rsp["files"];
	foreach($files as $file)
	{
		$LOCAL_DIR = "uploads/";
		if(!file_exists($LOCAL_DIR))
    	{
    		mkdir($LOCAL_DIR, 0777, true);
    	}
		
		$filename = $file["filename"];
		$fileurl = $file["url"];
		$filetime = $file["timestamp"];
		$filehash = $file["md5sum"];
		
		echo "Checking " . $filename . "\n";
		$local_filename = $LOCAL_DIR . $filename;
		
		// check if file exists
		$download_file = false;
		if(!file_exists($local_filename))
		{
			$download_file = true;
		}
		else
		{
			$local_filetime = filemtime($local_filename);
			$local_filehash = md5_file($local_filename);
			if($local_filehash != $filehash && $filetime >= $local_filetime)
			{
				$download_file = true;
			}
		}
		
		// download the file, if necessary
		if(!$download_file)
		{
			continue;
		}
		
		echo "Downloading " . $fileurl . "\n";
		file_put_contents($local_filename, fopen(file_url($fileurl), 'r'));
		if(file_exists($local_filename))
		{
			// set the timestamp
			touch($local_filename, $filetime);
		}
	}
}

?>