<?php
   function resizeImage($filename, $width_, $height_)
   {
      $width = $width_;
      $height = $height_;
      // Get new dimensions 
      list($width_orig, $height_orig) = getimagesize($filename); 
        
      $ratio_orig = $width_orig/$height_orig; 
        
      if ($width/$height > $ratio_orig) { 
          $width = $height*$ratio_orig; 
      } else { 
          $height = $width/$ratio_orig; 
      } 


      // Resampling the image  
      $image_p = imagecreatetruecolor($width_, $height_); 
      $red = imagecolorallocate($image_p, 255, 255, 255);
      imagefill($image_p, 0, 0, $red);

      $image = imagecreatefromjpeg($filename); 
      
      imagecopyresampled($image_p, $image, 0, 0, 0, 0, 
              $width, $height, $width_orig, $height_orig); 
      return $image_p;
   }

?>