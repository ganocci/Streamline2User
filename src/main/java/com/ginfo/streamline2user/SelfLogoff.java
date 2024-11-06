package com.ginfo.streamline2user;

import com.ginfo.streamline2user.utils.CertificateInstaller;
import com.ginfo.streamline2user.utils.CommandExecutor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelfLogoff extends JFrame {

    public SelfLogoff() {
        setTitle("G-Info Streamline");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton logoffButton = new JButton("Deslogar");
        logoffButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeLogoff();
            }
        });

        JButton installCertButton = new JButton("Instalar Certificado");
        installCertButton.setFont(new Font("Arial", Font.BOLD, 14));
        installCertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CertificateInstaller();
            }
        });

        JPanel panel = new JPanel();
        panel.add(logoffButton);
        panel.add(installCertButton);
        add(panel);

        setVisible(true);
    }

    private void executeLogoff() {
        try {
            String username = CommandExecutor.getCurrentUsername();
            String userId = CommandExecutor.getUserId(username);
            if (userId != null) {
                CommandExecutor.logoffUser(userId);
                JOptionPane.showMessageDialog(this, "Deslogado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Usuário não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao tentar deslogar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
