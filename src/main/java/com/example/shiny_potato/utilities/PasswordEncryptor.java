package com.example.shiny_potato.utilities;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Scanner;

public class PasswordEncryptor {
    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Scanner scanner = new Scanner(System.in);

        System.out.print("🔑 Inserisci la password da criptare: ");
        String password = scanner.nextLine();

        String encryptedPassword = passwordEncoder.encode(password);
        System.out.println("🔒 Password criptata: " + encryptedPassword);
    }
}

