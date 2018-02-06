package com.example.layout.assig16_1;
//Package objects contain version information about the implementation and specification of a Java package
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    //public keyword is used in the declaration of a class,method or field;public classes,method and fields can be accessed by the members of any class.
//extends is for extending a class. implements is for implementing an interface
//AppCompatActivity is a class from e v7 appcompat library. This is a compatibility library that back ports some features of recent versions of
// Android to older devices.
    private static final int WRITE_REQUEST_CODE = 50;
    EditText text;
    TextView content;
    Button add, delete;
    static String FILENAME = "test.txt";//creating a file
    File file;

    @RequiresApi(api = Build.VERSION_CODES.M)//requires the api version
    //Denotes that the annotated element should only be called on the given API level or higher.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Variables, methods, and constructors, which are declared protected in a superclass can be accessed only by the subclasses
        // in other package or any class within the package of the protected members class.
        //void is a Java keyword.  Used at method declaration and definition to specify that the method does not return any type,
        // the method returns void.
        //onCreate Called when the activity is first created. This is where you should do all of your normal static set up: create views,
        // bind data to lists, etc. This method also provides you with a Bundle containing the activity's previously frozen state,
        // if there was one.Always followed by onStart().
        //Bundle is most often used for passing data through various Activities.
// This callback is called only when there is a saved instance previously saved using onSaveInstanceState().
// We restore some state in onCreate() while we can optionally restore other state here, possibly usable after onStart() has
// completed.The savedInstanceState Bundle is same as the one used in onCreate().

        super.onCreate(savedInstanceState);
// call the super class onCreate to complete the creation of activity like the view hierarchy
        setContentView(R.layout.activity_main);
        //R means Resource
        //layout means design
        //  main is the xml you have created under res->layout->main.xml
        //  Whenever you want to change your current Look of an Activity or when you move from one Activity to another .
        // The other Activity must have a design to show . So we call this method in onCreate and this is the second statement to set
        // the design
        ///findViewById:A user interface element that displays text to the user.

        //UI components
        text = (EditText) findViewById(R.id.enter_data);
        content = (TextView) findViewById(R.id.show_data);
        add = (Button) findViewById(R.id.btn_add);
        delete = (Button) findViewById(R.id.btn_delete);


        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
       // requestPermissions(permissions, WRITE_REQUEST_CODE);

        // File creation
        file = new File(Environment.getExternalStorageDirectory(), FILENAME);
        try {
            if (file.createNewFile()){
                //creates the new file
                //A toast provides simple feedback about an operation in a small popup
                //Make a standard toast that just contains a text view with the text from a resource.

              //  Parameters
                //context	Context: The context to use. Usually your Application or Activity object.
                //resId	int: The resource id of the string resource to use. Can be formatted text.
                //duration	int: How long to display the message. Either LENGTH_SHORT or LENGTH_LONG
//show() methid:will shows the msg
                Toast.makeText(getApplicationContext(), "File Created", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();//if the file is not created
            //Prints this throwable and its backtrace to the standard error stream.
        }
        //update data to File
        //Register a callback to be invoked when this view is clicked. If this view is not clickable, it becomes clickable.
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String string = text.getText().toString();
                text.setText("");
//it will take the string value and read the filr
                ReadFile readFile = new ReadFile(file);
                readFile.execute(string);//executrs the string
            }
        });
        //deletion of  file
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                file.delete();
            }
        });

    }
    private class ReadFile extends AsyncTask<String, Integer, String> {
//this class is to read th file which is created
        File fileRead;

        public ReadFile(File fileRead) {
            super();
            this.fileRead=fileRead;

        }
        //update data to file
        @Override
        ////A user interface element that indicates the progress of an operation.
        //This method can be invoked from doInBackground(Params...) to publish updates on the UI thread while the background computation is still running.

        protected String doInBackground(String... strings) {
            String enter="\n";
            FileWriter filewriter=null;
            //he constructors of this class assume that the default character encoding and the default byte-buffer size are acceptable.
            try {
                filewriter=new FileWriter(fileRead,true);
                filewriter.append(strings[0].toString());
                //Constructs a FileWriter object given a file name with a boolean indicating whether or not to append the data written.
                filewriter.append(enter);
                filewriter.flush();
                //it deletes the file
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    filewriter.close();//closes the file
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
        //read data from file
        @Override
        //Runs on the UI thread after doInBackground(Params...). The specified result is the value returned by doInBackground(Params...).

        //This method won't be invoked if the task was cancelled.
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String name = "";
            StringBuilder stringBuilder = new StringBuilder();
            //class is used to create mutable (modifiable) string
            FileReader fileReader = null;
            try {
                fileReader = new FileReader(fileRead);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                //Reads text from a character-input stream, buffering characters so as to provide for the efficient reading of characters, arrays, and lines.
                while ((name = bufferedReader.readLine()) != null) {
                    //reads the line until it is not equal to null
                    stringBuilder.append(name);
                    stringBuilder.append("\n");

                }
                bufferedReader.close();
                fileReader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            content.setText(stringBuilder.toString());

        }
    }
    @Override
    //This interface is the contract for receiving the results for permission requests.
    //Parameters
   // requestCode	int: The request code passed in requestPermissions(android.app.Activity, String[], int)
    //permissions	String: The requested permissions. Never null.
    //grantResults	int: The grant results for the corresponding permissions which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case WRITE_REQUEST_CODE:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Class for retrieving various kinds of information related to the application packages that are currently installed on the device.
                    // You can find this class through getPackageManager().
                    //Granted.
                }
                else{
                    //Denied.
                }
                break;
        }
    }

}