package com.wfuertes;


import com.wfuertes.application.CreateUser;
import com.wfuertes.application.FetchUser;
import com.wfuertes.domain.entities.User;
import com.wfuertes.infrastructure.ConnectionFactory;
import com.wfuertes.infrastructure.SqliteUserRepository;

public class App {

    public static void main(String[] args) {
        var userRepository = new SqliteUserRepository(new ConnectionFactory());

        // Create user: use case
        var createUser = new CreateUser(userRepository);
        var user = User.create("wfuertes@gmail.com", "pa55w0rd");
        createUser.execute(user);

        // Fetch user: use case
        var fetchUser = new FetchUser(userRepository);
        var savedUser = fetchUser.execute(user.id()).orElseThrow(() -> new RuntimeException("User not found"));
        
        System.out.println(savedUser);
    }

}
