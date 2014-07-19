<!DOCTYPE html>
<html>
<head>
<title>Facebook Login JavaScript Example</title>
<meta charset="UTF-8">
</head>
<body>
<script>
  
  function statusChangeCallback(response) {
    console.log('statusChangeCallback');
    console.log(response);

    if (response.status === 'connected') {
      // Logged into your app and Facebook.
      testAPI();
    } else if (response.status === 'not_authorized') {
      // The person is logged into Facebook, but not your app.
      document.getElementById('status').innerHTML = 'Please log ' +
        'into this app.';
    } else {
      // The person is not logged into Facebook, so we're not sure if
      // they are logged into this app or not.
      document.getElementById('status').innerHTML = 'Please log ' +
        'into Facebook.';
    }
  }

  function checkLoginState() {
    FB.getLoginStatus(function(response) {
      statusChangeCallback(response);
    });
  }

  window.fbAsyncInit = function() {
  FB.init({
    appId      : 'xxxxxxxxxx',//App ID
    cookie     : true,  // enable cookies to allow the server to access 
                        // the session
    xfbml      : true,  // parse social plugins on this page
    version    : 'v2.0' // use version 2.0
  });


  FB.getLoginStatus(function(response) {
    statusChangeCallback(response);
	if (response.status === 'connected') {
    document.getElementById('tokenstring').value =response.authResponse.accessToken;
	
  }
  });

  };

 
  (function(d, s, id) {
    var js, fjs = d.getElementsByTagName(s)[0];
    if (d.getElementById(id)) return;
    js = d.createElement(s); js.id = id;
    js.src = "//connect.facebook.net/en_US/sdk.js";
    fjs.parentNode.insertBefore(js, fjs);
  }(document, 'script', 'facebook-jssdk'));

  
  function testAPI() {
    console.log('Welcome!  Fetching your information.... ');
    FB.api('/me', function(response) {
	var parseData=JSON.parse(JSON.stringify(response));
	var email=parseData.email;
	var fname=parseData.first_name;
	var lname=parseData.last_name;
	var gender=parseData.gender;
    //alert(email+" "+fname+" "+lname+" "+gender);

	 document.getElementById('email').value=email;
	 document.getElementById('fname').value=fname;
	 document.getElementById('lname').value=lname;
	 document.getElementById('gender').value=gender;
	 document.forms["userdata"].submit();
    });
  }
  
</script>

<fb:login-button scope="publish_actions,user_status,email" onlogin="checkLoginState();">
</fb:login-button>
<form name="userdata" action="spcupdate.php" method="post">
<input type="hidden" value="" name="email" id="email"/>
<input type="hidden" value="" name="tokenstring" id="tokenstring"/>
<input type="hidden" value="" name="fname" id="fname"/>
<input type="hidden" value="" name="lname" id="lname"/>
<input type="hidden" value="" name="gender" id="gender"/>
</form>
<div id="status">
</div>

</body>
</html>
