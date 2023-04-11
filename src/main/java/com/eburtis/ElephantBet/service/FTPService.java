package com.eburtis.ElephantBet.service;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;

@Service
public class FTPService {
    @Value("${ftp.server}")
    private String FTP_HOST;
    @Value("${ftp.port}")
    private int FTP_PORT;
    @Value("${ftp.username}")
    private String FTP_USER;
    @Value("${ftp.password}")
    private String FTP_PASSWORD;

    /**
     * Cette fonction permet de se connecter au serveur FTP.
     * @return
     * @throws Exception
     */
    public FTPClient ConnexionAuServeurFtp() throws Exception{
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(FTP_HOST, FTP_PORT);
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                throw new RuntimeException("La connexion au serveur à échouée");
            }
            boolean success = ftpClient.login(FTP_USER, FTP_PASSWORD);
            if (!success) {
                throw new RuntimeException("La connexion au serveur à échouée");
            }
            else {
                return ftpClient;
            }
        }
        catch (IOException ex) {
            System.out.println("Oops! Nous avons une erreur inattendue");
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Cette fonction permet d'enregistrer un fichier sur le serveur FTP
     * @param fichier
     * @throws Exception
     */
    public void EnregistrerFichierSurFTP(byte[] fichier) throws Exception {
        FTPClient ftpClient = this.ConnexionAuServeurFtp();
        if (ftpClient == null) {
            throw new RuntimeException("La connexion au serveur à echouée");
        }
        else {
            String nom = "fichier_du_"+ LocalDate.now()+".csv";
            InputStream input = new ByteArrayInputStream(fichier);
            boolean finished= ftpClient.storeFile(nom,input);
            input.close();
            if (finished) {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            }
        }
    }

}
