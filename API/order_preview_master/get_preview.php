<?php
	include 'get_img_urls.php';
	include 'image_resizer.php';
	error_reporting(E_ERROR | E_PARSE);

	$dest = imagecreatefrompng('source.png');
	imagealphablending( $dest, false );
	imagesavealpha( $dest, true );

	$size = 2;
	$padding = 2;

	$w = (200-($padding*$size-1)) / $size;
	$h = (200-($padding*$size-1)) / $size;

	$limit = $size*$size;
	$counter = 0;
	$x = 0;
	$y = 0;
	foreach ($images as $image) {
		if ($counter <= $limit) {
			$pic = resizeImage("http://handyway.uz/media/".$image, $w, $h);
			imagecopymerge($dest, $pic, $x+$padding, $y+$padding, 0, 0, $w, $h, 100);
			$x+=$w;
			if ($x/$w >= $size) {
				$x = 0;
				$y += $h;
			}
			$counter += 1;
		}else{
			break;
		}
	}
	
	
	header('Content-Type: image/png');
	imagepng($dest);

	imagedestroy($dest);
	imagedestroy($src);
?>