package com.example.playfair;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    EditText e1,e2,e3;
    char[][] key_matrix=new char[5][5];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e1=(EditText) findViewById(R.id.et1);
        e2=(EditText) findViewById(R.id.et2);
        e3=(EditText) findViewById(R.id.et3);
    }
    public void key_mat()
    {
        int i,j,k=0,l,flag=1,res_i=0,res_j=0;
        String key_in=e2.getText().toString();
        char[] key_arr=key_in.toCharArray();
        int key_length=key_arr.length;
        String key="";
        for(i=0;i<key_length;i++)
        {
            for(j=0;j<i;j++)
            {
                if(key_arr[i]==key_arr[j])
                {
                    break;
                }
            }
            if(j==i){
                key+=key_arr[i];
            }
        }
        key+=" ";
        char c;

        Set<Character> hs1 = new HashSet<Character>();
        for(c='a';c<='z';c++)
        {
            hs1.add(c);
        }
        Set<Character> hs2 = new HashSet<Character>();
        for(char ch:key.toCharArray())
        {
            hs2.add(ch);
        }
        hs1.removeAll(hs2);
        List<Character> ref = new ArrayList<Character>(hs1);
        for(i=0;i<5;i++)
        {
            for(j=0;j<5;j++)
            {
                c=key.charAt(k);
                key_matrix[i][j]=c;
                k++;
                if(key.charAt(k)==' ')
                {
                    flag=0;
                    res_i=i;
                    res_j=j+1;
                    break;
                }
            }
            if(flag==0)
                break;
        }
        k=0;
        for(i=res_i;i<5;i++)
        {
            if(i==res_i)
                j=res_j;
            else
                j=0;
            for(;j<5;j++)
            {
                key_matrix[i][j]=ref.get(k);
                k++;
            }
        }
    }
    public void encrypt(View v)
    {
        //splitting plaintext
        String message=e1.getText().toString().toLowerCase();
        if (message.isEmpty()||e2.getText().toString().isEmpty()||(message.isEmpty()&&e2.getText().toString().isEmpty()))
        {
            Toast.makeText(this, "Enter required values", Toast.LENGTH_SHORT).show();
            return;
        }
        message = message.replaceAll("\\s", "");
        message+="x";
        char[] str=message.toCharArray();
        String[] pt=new String[str.length];
        int i,j,k=0,l;
        for(i=0;i<str.length-1;)
        {
            if(str[i]==str[i+1])
            {
                pt[k]=str[i]+"x";
                i++;
            }
            else
            {
                pt[k]=String.valueOf(str[i]);
                pt[k]+=String.valueOf(str[i+1]);
                i+=2;
            }
            k++;
        }
        //converting back to string
        StringBuffer sb = new StringBuffer();
        for(i = 0; i < k; i++) {
            sb.append(pt[i]);
        }
        String ps = sb.toString();
        //encrypting
        key_mat();
        str=ps.toCharArray();
        StringBuilder ct = new StringBuilder();
        k = 0;
        while (k < str.length - 1)
        {
            // Find the positions of the two characters in the key matrix
            int row1 = -1, col1 = -1, row2 = -1, col2 = -1;
            for (i = 0; i < 5; i++)
            {
                for (j = 0; j < 5; j++)
                {
                    if (str[k] == key_matrix[i][j])
                    {
                        row1 = i;
                        col1 = j;
                    }
                    if (str[k + 1] == key_matrix[i][j])
                    {
                        row2 = i;
                        col2 = j;
                    }
                }
            }
            // Determine the replacement letters based on Playfair cipher rules
            char c1, c2;
            if (row1 == row2)
            {  // Same row
                c1 = key_matrix[row1][(col1 + 1) % 5];
                c2 = key_matrix[row2][(col2 + 1) % 5];
            }
            else if (col1 == col2)
            {  // Same column
                c1 = key_matrix[(row1 + 1) % 5][col1];
                c2 = key_matrix[(row2 + 1) % 5][col2];
            }
            else
            {  // Different row and column
                c1 = key_matrix[row1][col2];
                c2 = key_matrix[row2][col1];
            }

            // Append the encrypted characters to the result
            ct.append(c1);
            ct.append(c2);

            k += 2;
        }

        e3.setText(ct.toString());
    }
    public void decrypt(View v)
    {
        String ct = e3.getText().toString().toLowerCase();
        if (ct.isEmpty()||e2.getText().toString().isEmpty()||(ct.isEmpty()&&e2.getText().toString().isEmpty()))
        {
            Toast.makeText(this, "Enter required values", Toast.LENGTH_SHORT).show();
            return;
        }
        char[] str = ct.toCharArray();
        StringBuilder dt = new StringBuilder();
        key_mat();
        int i, j, k = 0;
        while (k < str.length - 1)
        {
            // Find the positions of the two characters in the key matrix
            int row1 = -1, col1 = -1, row2 = -1, col2 = -1;
            for (i = 0; i < 5; i++)
            {
                for (j = 0; j < 5; j++)
                {
                    if (str[k] == key_matrix[i][j])
                    {
                        row1 = i;
                        col1 = j;
                    }
                    if (str[k + 1] == key_matrix[i][j])
                    {
                        row2 = i;
                        col2 = j;
                    }
                }
            }

            // Determine the decrypted letters based on Playfair cipher rules
            char d1, d2;
            if (row1 == row2) {  // Same row
                d1 = key_matrix[row1][(col1 + 4) % 5];
                d2 = key_matrix[row2][(col2 + 4) % 5];
            }
            else if (col1 == col2)
            {  // Same column
                d1 = key_matrix[(row1 + 4) % 5][col1];
                d2 = key_matrix[(row2 + 4) % 5][col2];
            }
            else
            {  // Different row and column
                d1 = key_matrix[row1][col2];
                d2 = key_matrix[row2][col1];
            }

            // Append the decrypted characters to the result
            dt.append(d1);
            dt.append(d2);

            k += 2;
        }

        e1.setText(dt.toString());
    }
}