package actions;

import interfaces.Database;
import models.Agent;
import models.House;
import program.Message;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAction extends UnicastRemoteObject implements Database {

    private Connection connection;

    public DatabaseAction() throws RemoteException {
    }

    public void createConnection() {
        //db info
        String username = "root";
        String password = "MySQL";
        String host = "localhost";

        String dbName = "houserental";

        //create connection

        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + dbName,
                    username, password);
            Message.server("Connected to Database Successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getNextImage() throws RemoteException{

        //get current number add 1 save new number and return
        String query = "SELECT value FROM misc where name = 'image'";

        try {
            Statement stmt = connection.createStatement();

            ResultSet resultSet = stmt.executeQuery(query);

            String data;

            //get current
            resultSet.next();
            data = resultSet.getString("value");

            //create next
            int next = Integer.parseInt(data);
            next += 1;
            data = String.valueOf(next);

            query = "UPDATE misc SET value ='" + data +"' WHERE name = 'image'";
            stmt.executeUpdate(query);

            return data;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Integer> getListings(int category) throws RemoteException {

        Message.client("Get Listing IDs Request.");

        List<Integer> list = new ArrayList<>();
        String query = "SELECT id FROM house";

        if (category != 0)
            query += " WHERE category = " + category;

        try {
            Statement stmt = connection.createStatement();

            ResultSet resultSet = stmt.executeQuery(query);

            while (resultSet.next()){
                list.add(resultSet.getInt("id"));
            }

            Message.server("Sending Listing Results.");

        } catch (Exception ex){
            ex.printStackTrace();

        }

        return list;
    }

    @Override
    public List<Integer> getListingsbyAgent(int agentId) throws RemoteException {

        Message.client("Get Listing IDs by Agent Request.");

        List<Integer> list = new ArrayList<>();
        String query = "SELECT id FROM house WHERE agent = " + agentId;

        try {
            Statement stmt = connection.createStatement();

            ResultSet resultSet = stmt.executeQuery(query);

            while (resultSet.next()){
                list.add(resultSet.getInt("id"));
            }

            Message.server("Sending Listing Results.");

        } catch (Exception ex){
            ex.printStackTrace();

        }

        return list;
    }

    @Override
    public int login(String username, String password) throws RemoteException {

        String query = "SELECT id FROM agent WHERE username = ? AND password = ?";
        try {

            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet result = stmt.executeQuery();


            if (result.next()) {
                return result.getInt("id");
            } else
                return 0;

        }catch (Exception ex){
            ex.printStackTrace();
        }

        return 0;
    }

    @Override
    public House getListingDetail(int id) throws RemoteException {

        House house = new House();

        String query = "SELECT * FROM house WHERE id = " + id;

        try{

            Statement stmt = connection.createStatement();

            ResultSet resultSet = stmt.executeQuery(query);

            if (resultSet.next()){
                house.setId(resultSet.getInt("id"));
                house.setTitle(resultSet.getString("title"));
                house.setPrice(resultSet.getInt("price"));
                house.setCategory(resultSet.getInt("category"));
                house.setFeatures(resultSet.getString("features"));
                house.setImageid(resultSet.getString("image"));

                Agent agent = getAgentDetail(resultSet.getInt("agent"));
                house.setAgent(agent);

                return house;

            }

        }catch (Exception ex){
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    public Agent getAgentDetail(int id) throws RemoteException {

        Agent agent = new Agent();

        String query = "SELECT * FROM agent WHERE id = " + id;

        try{

            Statement stmt = connection.createStatement();

            ResultSet resultSet = stmt.executeQuery(query);

            if (resultSet.next()){

                agent.setId(resultSet.getInt("id"));
                agent.setName(resultSet.getString("name"));
                agent.setEmail(resultSet.getString("email"));
                agent.setPhone(resultSet.getInt("phone"));
                agent.setUsername(resultSet.getString("username"));
                agent.setPassword(resultSet.getString("password"));

                return agent;

            }

        }catch (Exception ex){
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    public int saveAgent(Agent agent) throws RemoteException {

        String query = "UPDATE agent SET name = ?, phone = ?, email = ?, username = ?, password = ? WHERE id = ?";

        try {

            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, agent.getName());
            stmt.setInt(2, agent.getPhone());
            stmt.setString(3, agent.getEmail());
            stmt.setString(4, agent.getUsername());
            stmt.setString(5, agent.getPassword());
            stmt.setInt(6, agent.getId());

            return stmt.executeUpdate();


        }catch (Exception ex){
            ex.printStackTrace();
        }

        return -1;
    }

    @Override
    public int saveHouse(House house) throws RemoteException {

        String query = "UPDATE house SET title = ?, price = ?, category = ?, features = ?, agent = ?, image = ? WHERE id = ?";

        try {

            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, house.getTitle());
            stmt.setInt(2, house.getPrice());
            stmt.setInt(3, house.getCategory());
            stmt.setString(4, house.getFeatures());
            stmt.setInt(5, house.getAgent().getId());
            stmt.setString(6, house.getImageid());
            stmt.setInt(7, house.getId());

            return stmt.executeUpdate();


        }catch (Exception ex){
            ex.printStackTrace();
        }

        return -1;
    }

    @Override
    public int addHouse(House house) throws RemoteException {

        String query = "INSERT INTO house (title, price, category, features, agent, image) Values (?, ?, ?, ?, ?, ?)";

        try {

            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setString(1, house.getTitle());
            stmt.setInt(2, house.getPrice());
            stmt.setInt(3, house.getCategory());
            stmt.setString(4, house.getFeatures());
            stmt.setInt(5, house.getAgent().getId());
            stmt.setString(6, house.getImageid());

            return stmt.executeUpdate();


        }catch (Exception ex){
            ex.printStackTrace();
        }

        return -1;
    }

    @Override
    public int deleteHouse(int id) throws RemoteException {

        String query = "DELETE FROM house WHERE id  = " + id;

        try{
            Statement stmt = connection.createStatement();

            return stmt.executeUpdate(query);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return -1;
    }

}
