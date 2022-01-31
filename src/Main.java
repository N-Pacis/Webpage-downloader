
import Helpers.ErrorMessageLogger;
import Helpers.SuccessMessageLogger;
import Helpers.WarningMessageLogger;

import java.io.*;
import java.net.URL;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);
        WarningMessageLogger warning = new WarningMessageLogger();
        SuccessMessageLogger success = new SuccessMessageLogger();

        System.out.println("Enter the website you want to clone(format: https://website.com)");
        String baseUrl = scan.nextLine();
        URL url = new URL(baseUrl);

        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

        Set<String> webpageLinks = new HashSet<String>();

        String link;
        while((link = reader.readLine()) != null) {
            if (link.contains("href=") && link.contains("<a") && !(link.contains("http") || link.contains("https"))) {
                String anchor = link.split("href")[1].split("=\"")[1].split("\"")[0] + "\n";
                if (!(anchor.equals("./") || anchor.contains("#") || anchor.contains("mailto") || anchor.isEmpty())) {
                    webpageLinks.add(anchor);
                }
            }
        }
        String[] webLinks = webpageLinks.toArray(new String[webpageLinks.size()]);
        reader.close();
        for(int i=0;i<webLinks.length;i++){
            webLinks[i] = webLinks[i].replace("../","");
            webLinks[i] = webLinks[i].replace("\n","");
            url = new URL(baseUrl+"/"+webLinks[i]);
            BufferedReader lineCounter = new BufferedReader(new InputStreamReader(url.openStream()));
            Integer count = 0;
            while( lineCounter.readLine() != null){
                count++;
            }
            lineCounter.close();

            BufferedReader webReader = new BufferedReader(new InputStreamReader(url.openStream()));
            webLinks[i] = webLinks[i].equals("") ? "home.html" : webLinks[i].split("\\.")[0] + ".html";
            System.out.println("Filename:"+webLinks[i]);
            File file = new File("Storage/"+webLinks[i]);
            if(!file.exists()){
                if(file.getParentFile() != null){
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter("Storage/"+webLinks[i]));

            warning.log("Downloading "+webLinks[i].replace("./",""));

            String line;
            Integer currentLineCount = 0;
            Double percentage = 0.0;

            while((line = webReader.readLine()) != null){
                currentLineCount++;
                percentage = ((double)currentLineCount/(double)count) * 100.0;
                System.out.println(String.format("Downloading %.2f",percentage)+"%");
                writer.write(line+"\n");
            }
            webReader.close();
            writer.close();
            success.log("Downloaded "+webLinks[i].replace("./","") +" successfully");
        }
        success.log("Downloaded the whole website successfully");
    }
}
