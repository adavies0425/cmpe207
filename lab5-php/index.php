<?php
    // For Debugging
    //error_reporting(E_ALL);
    //ini_set('display_errors', '1');

    $domain = "mysql";
    $username = "cmpe207";
    $password = "spring2014";
    $database = "lab5";
    $dbtable = "userinfo";

    // Create connection
    $connection = mysqli_connect($domain, $username, $password, $database);

    // Check connection
    if( mysqli_connect_errno() )
    {
        echo "<html><head><title>Neatwurk.com</title></head>";
        echo "<body><h1>Error!</h1><p>";
        echo "Failed to connect to MySQL: " . mysqli_connect_error();
        echo "</p></body>";
        echo "</head></html>";
        exit(0);
    }

    // Look at query string
    if(isset($_GET["id"]))
    {
        $infoid = $_GET["id"];
        if($infoid === "all")
        {
            echo "<html><head><title>CMPE207 - Lab 5</title></head><body>";
            echo "<h1>User Feedback</h1>";

            // print all from the db
            $query = "SELECT * FROM $dbtable";
            $result = mysqli_query($connection, $query);
            echo "<table border='1'><tr>";
            echo "<th>id</th>";
            echo "<th>First Name</th>";
            echo "<th>Last Name</th>";
            echo "</tr>";

            // in case of no results
            if(count($result) == 0)
            {
                echo "<p><em>No results.</em></p>";
            }
            else
            {
                while($row = mysqli_fetch_array($result))
                {
                    $url = "/lab5/index.php?id=" . $row['id'];
                    echo "<tr>";
                    echo "<td><a href='$url'>" . $row['id'] . "</a></td>";
                    echo "<td>" . $row['first_name'] . "</td>";
                    echo "<td>" . $row['last_name'] . "</td>";
                    echo "</tr>";
                }
            }

            echo "</table>";
            echo "</body></html>";
        }
        else if($infoid === "new")
        {
            if($_SERVER['REQUEST_METHOD'] === 'POST')
            {
                // process form
                $first_name = $_POST["first_name"];
                $last_name = $_POST["last_name"];
                $authorize = $_POST["authorize"]? "1" : "0";
                $gender = ($_POST["gender"] === "0")? "Male" : "Female";
                $suggestion = $_POST["suggestion"];

                // query
                $query = "INSERT INTO `$dbtable` (`first_name`, `last_name`, `gender`, `authorize`, `suggestion`, `id`) VALUES ('$first_name', '$last_name', '$authorize', '$gender', '$suggestion', NULL);";
                mysqli_query($connection, $query);

                // display thank you message
                echo "<html><head><title>Thank You!</title></head>";
                echo "<body><h1>Thank You!</h1><p>Your feedback has been logged. ";
                echo "<a href='/lab5/index.php'>Fill out another survey</a>, or ";
                echo "<a href='/lab5/index.php?id=all'>view all responses.</a></p></body></html>";
            }
            else
            {
                // show form
?>

<html>
    <head><title>User Feedback</title></head>
    <body>
    <h3>User Survey</h3>
    <form name="survey_form" method="POST" action="/lab5/index.php?id=new">
        <label>
            <input type="checkbox" name="authorize" id="authorize">
            I authorize the company to use my data.
        </label><br/><br/>
        <label>
            First Name:<br/>
            <input type="text" name="first_name" id="first_name">
        </label><br/><br/>
        <label>
            Last Name:<br/>
            <input type="text" name="last_name" id="last_name">
        </label><br/><br/>
        <label>
            <input type="radio" name="gender" id="Male" value="Male">Male<br/>
            <input type="radio" name="gender" id="Female" value="Female">Female
        </label><br/><br/>
        <label>
            Suggestion<br/>
            <textarea name="suggestion" id="suggestion" cols=45 rows=5></textarea>
        </label>
        <p>&nbsp;</p>
        <input type="submit" value="Submit">
    </form>
    </body>
</html>

<?php
            }
        }
        else
        {
            // print all from the db
            $query = "SELECT * FROM $dbtable WHERE id=$infoid";
            $result = mysqli_query($connection, $query);

            $title = "Not Found";
            // in case we can't find it
            if(count($result) == 0)
            {
                echo "<html><head><title>$title</title></head><body>";
                echo "<h1>$title</h1>";
            }
            else
            {
                while($row = mysqli_fetch_array($result))
                {
                    $first_name = $row['first_name'];
                    $last_name = $row['last_name'];
                    $gender = $row['gender'];
                    $authorize = $row['authorize'];
                    $authtext = ($authorize == 1)? "Yes" : "No";
                    $suggestion = $row['suggestion'];
                    $title = "$first_name $last_name";
                    echo "<html><head><title>$title</title></head></body>";
                    echo "<h1>$title</h1>";
                    echo "<p><strong>Gender:</strong> $gender <br/>";
                    echo "<strong>Authorize to use data:</strong> $authtext</p>";
                    echo "<p><strong>Suggestion:</strong> <em>$suggestion</em></p>";
                    echo "</body></html>";
                }
            }
        }
    }
    else
    {
?>
<html><head><title>User Feedback</title></head>
<body><h3>User Feedback</h3>
<p>Options:
<ul>
    <li><a href="/lab5/index.php?id=all">View all responses</a></li>
    <li><a href="/lab5/index.php?id=new">Fill out a survey</a></li>
</ul>
</p></body></html>
<?php
    }

    // Close Connection
    mysqli_close($connection);
?>

