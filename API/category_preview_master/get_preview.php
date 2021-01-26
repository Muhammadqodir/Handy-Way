<?php
	include 'get_img_urls.php';
	include 'image_resizer.php';
	error_reporting(E_ERROR | E_PARSE);

	$dest = imagecreatefrompng('source.png');
	imagealphablending( $dest, false );
	imagesavealpha( $dest, true );

	$pics = array();

	$size = 3;

	$padding = 3;


	$w = (200-($padding*$size)) / $size;
	$h = (200-($padding*$size)) / $size;

	for ($i=0; $i < count($images); $i++) {
		$pics[] = resizeImage("http://handyway.uz/media/".$images[$i], $w, $h);
	}

	$i = 1;
	$image = 0;

	$y = 0+$padding;
	while ($i <= $size) {
		$x = 200;
		for ($j=0; $j < $i; $j++) { 
			imagecopymerge($dest, $pics[$image], $x-$w, $y, 0, 0, $w, $h, 100);
			$image += 1;
			$x -= $w+$padding;
		}
		$i += 1;
		$y += $h+$padding;
	}
	
	
	header('Content-Type: image/png');
	imagepng($dest);

	imagedestroy($dest);
	imagedestroy($src);

	


?>