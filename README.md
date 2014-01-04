This code is not production ready. It is meant to help you integrate quickly with SendGrid when developing for Android natively. You will need to incorporate error handling and testing.

There is a companion blog post, yet to published, that walks through the entire process to build this app from scratch. When it's live, I'll post the link here.

This project was born out of the [Android Developer training](http://developer.android.com/training/basics/firstapp/index.html).

## Quickstart

The easiest way to get started with this example is to:

* Clone the repo
* Modify the UtilsDEFAULT.java file as described in the file
* Compile and run the code

## Sending Email

* Make sure you include the `uses-permissions` tag in your AndroidManifest.xml like [so](https://github.com/thinkingserious/sendgrid-android-example/blob/master/AndroidManifest.xml).
* Check out the code in [MainActivity.java](https://github.com/thinkingserious/sendgrid-android-example/blob/master/src/com/thinkingserious/sendgrid/MainActivity.java)
* Review the [SendGrid Java library](https://github.com/sendgrid/sendgrid-java).

## Retrieving Statistics

* Review the code in [DisplayMessageActivity.java](https://github.com/thinkingserious/sendgrid-android-example/blob/master/src/com/thinkingserious/sendgrid/DisplayMessageActivity.java)
* Review the [SendGrid Web API stats endpoint](http://sendgrid.com/docs/API_Reference/Web_API/Statistics/index.html).

## Info & Help

If you create something cool, let us know so we can include you in the [SendGrid Developer Community](http://sendgrid.com/developers/developers).

This is my first native Android (and Java) app. I assume I broke all sorts of conventions. Please let me know how I can improve this tutorial with a pull request or open an issue. Thanks! 
