<?php

if($_SERVER['REQUEST_METHOD'] === 'POST')
{
    if($_FILES["upload"]["error"] > 0)
    {
?>
<html><body><h1>Error!</h1><h3><?php echo("" . $_FILES["upload"]["error"]); ?></h3></body></html>
<?php
    }
    else
    {
        $filename = $_FILES["upload"]["name"];
        $filesize = $_FILES["upload"]["size"];
        $tmpfile = $_FILES["upload"]["tmp_name"];
        move_uploaded_file($tmpfile, "uploads/" . $filename);
?>

<html>
    <body>
        <h1>Success!</h1>
        <p>Successfully uploaded file to <a href="uploads/<?php echo($filename); ?>">uploads/<?php echo($filename); ?></a> (<?php echo($filesize); ?> bytes).</p>
        <pre>
        <?php //print_r($_FILES); print_r($_POST); print_r(get_defined_vars()); ?>
        </pre>
    </body>
</html>

<?php
    }
}
else
{
?>

<html>
    <body>
        <form action="index.php" method="POST" enctype="multipart/form-data">
            <label for="upload">File 1:</label>
            <input type="file" name="upload" id="upload" /><br/>
            <input type="submit" name="submit" value="submit" />
        </form>
    </body>
</html>

<?php
}

?>
