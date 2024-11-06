package com.ginfo.streamline2user.utils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class CertificateInstaller extends JFrame {

    private JTextField passwordField;
    private JTextField filePathField;
    private File selectedFile;

    public CertificateInstaller() {

        setTitle("Instalação de Certificado");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);


        filePathField = new JTextField(20);
        filePathField.setEditable(false);


        JLabel passwordLabel = new JLabel("Senha:");
        passwordField = new JPasswordField(20);

        JButton selectFileButton = new JButton("Selecionar Arquivo");
        selectFileButton.addActionListener(e -> selectFile());

        JButton installButton = new JButton("Instalar Certificado");
        installButton.addActionListener(e -> installCertificate());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        JPanel filePanel = new JPanel();
        filePanel.add(selectFileButton);
        filePanel.add(filePathField);
        panel.add(filePanel);

        JPanel passwordPanel = new JPanel();
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        panel.add(passwordPanel);

        panel.add(installButton);

        add(panel);
        setVisible(true);
    }

    private void selectFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivos PFX", "pfx");
        fileChooser.setFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            filePathField.setText(selectedFile.getAbsolutePath());
            JOptionPane.showMessageDialog(this, "Arquivo selecionado: " + selectedFile.getName());
        } else {
            JOptionPane.showMessageDialog(this, "Seleção de arquivo cancelada.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void installCertificate() {
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um arquivo.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String password = passwordField.getText();
        String certFilePath = selectedFile.getAbsolutePath();

        try {
            String command = String.format(
                    "powershell.exe -Command \"" +
                            "$certPassword = ConvertTo-SecureString -String '%s' -Force -AsPlainText; " +
                            "$cert = Import-PfxCertificate -FilePath '%s' -CertStoreLocation Cert:\\CurrentUser\\My -Password $certPassword -Exportable; " +
                            "if ($cert -eq $null) {exit 1} else {exit 0}\"",
                    password, certFilePath
            );

            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                JOptionPane.showMessageDialog(this, "Senha incorreta ou erro ao instalar o certificado.", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Certificado instalado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao instalar o certificado: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
