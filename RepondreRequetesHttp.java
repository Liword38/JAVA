package exo;

import java.io.*;
import java.net.*;

public class RepondreRequetesHttp {

    public static void main(String[] args) throws IOException {
        ServerSocket s = new ServerSocket(8080);
        String reponseContent = "";
        while (true) {
            Socket c = s.accept();
            BufferedReader r = new BufferedReader(new InputStreamReader(c.getInputStream()));
            BufferedWriter w = new BufferedWriter(new OutputStreamWriter(c.getOutputStream()));
            String line = r.readLine();
            System.out.println("la première ligne est:"+line);


			if (line.substring(0, 3).equals("GET")) {
                String chemin = line.replace("GET ", "");
                chemin = chemin.replace(" HTTP/1.1", "");
                File le_fichier = new File(chemin);
              
                System.out.println("On cherche:"+chemin);
                
                System.out.println("qui se trouve:"+le_fichier.getAbsolutePath());
                if (le_fichier.isDirectory()) {
                	System.out.println("On est dans un dossier contenant :");
                	System.out.println();
                    String liste[] = le_fichier.list();
                    w.write("HTTP/1.1 200 OK \n Content-Type: text/html \n");
                    w.write("\n");
                    
                    if (liste != null) {
                        for (int i = 0; i < liste.length; i++) {
                        	reponseContent += liste[i]+"<br>";
                        ;
                            System.out.println(liste[i]);
                            
                            
                        }
                        w.write("<html> <body><h1>"+ reponseContent+ "<h1></body></html>");
                    }
                    w.write("\n");

                }
                else if(le_fichier.isFile()) {
                	System.out.println("On est dans un fichier contenant :");
                	System.out.println();
                	InputStream flux=new FileInputStream(le_fichier); 
                	InputStreamReader lecture=new InputStreamReader(flux);
                	BufferedReader buff=new BufferedReader(lecture);
                	String ligne;
                	w.write("HTTP/1.1 200 OK \n Content-Type: text/html \n");
                    w.write("\n");

                	while ((ligne=buff.readLine())!=null){
                		System.out.println(ligne);
                		w.write(ligne + "\n");
                	}
                	buff.close(); 
                }
                else {
                	System.out.println("ERREUR 404: La page demandée n'existe pas");
                	w.write("HTTP/1.1 200 OK \n Content-Type: text/html \n");
                	w.write("\n");
                	w.write("<html>	<head> <title>404 Not Found</title> </head><body> <h1>Not Found</h1> <p>The requested URL" +chemin+ " was not found on this server.</p> </body> </html>");
                }
                System.out.println();
                
                
            }
			
			w.close();
			c.close();
        }
        
    }
    

}

//
// TANT QUE (vrai) FAIRE
// accepter connexion
// récupérer flux d'entrée et flux de sortie
// lire la première ligne sur flux d'entrée
// SI (méthode get) ALORS
// récupérer le chemin dans la première ligne
// SI (CHEMIN est un répertoire) ALORS
// construire réponse affichant le contenu du répertoire
// SINON SI (CHEMIN est un fichier)
// construire réponse avec le contenu du fichier
// SINON
// envoyer erreur 404
// FIN SI
// fermer la connexion
// FIN TANT QUE
