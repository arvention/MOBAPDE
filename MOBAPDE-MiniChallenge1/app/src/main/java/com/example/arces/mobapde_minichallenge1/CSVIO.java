package com.example.arces.mobapde_minichallenge1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class CSVIO {
	public ArrayList<String> read(String filename, String delimeter){
		ArrayList<String> input = new ArrayList<>();
		
		try {
            File f = new File(filename);
            if(!f.exists())
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            Scanner sc = new Scanner(f);
			sc.useDelimiter(delimeter);
			while(sc.hasNext()){
				input.add(sc.nextLine());
			}
			sc.close();
		} catch (FileNotFoundException e) {
            e.printStackTrace();
		}
		
		return input;
	}
	
	public void write(ArrayList<String> output, String filename, String delimeter){
		
		try {
			FileWriter fw = new FileWriter(filename);
			for(String s : output){
				String[] line = s.split(delimeter);
				for(String i : line){
					fw.append(i);
					fw.append(delimeter);
				}
				fw.append("\n");
			}
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
