package actions;

import interfaces.Database;
import interfaces.Server;
import models.Agent;
import models.House;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ServerAction extends UnicastRemoteObject implements Server {

    Database db;
    String dir = "src/images/";

    public ServerAction(Database db) throws RemoteException{
        this.db = db;
    }

    @Override
    public Agent loadAgent(int id) throws RemoteException {
        return db.getAgentDetail(id);
    }

    @Override
    public boolean saveAgent(Agent agent) throws RemoteException {
        if (db.saveAgent(agent) == -1)
            return false;
        else
            return true;
    }

    @Override
    public House loadHouse(int id) throws RemoteException {
        House house = db.getListingDetail(id);

        if (house.getImageid() != null && house.getImageid().length() > 0){
            File file = new File(dir + house.getImageid());

            if (file.exists()) {
                house.setImage(file);
            }
        }

        return house;
    }

    @Override
    public boolean saveHouse(House house) throws RemoteException {
        House temp = db.getListingDetail(house.getId());

        if (!temp.getImageid().equals(house.getImageid())){

            if(house.getImagebytes().length > 0) {
                String imgName = db.getNextImage() + house.getImageid();

                Path path = Paths.get(dir + imgName);
                try {
                    Files.write(path, house.getImagebytes());
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }

                File fileDel = new File(dir + temp.getImageid());
                if(fileDel.exists())
                    fileDel.delete();

                house.setImageid(imgName);
            }
        }

        if (db.saveHouse(house) == -1)
            return false;
        else
            return true;
    }

    @Override
    public boolean addHouse(House house) throws RemoteException {

        if(house.getImagebytes().length > 0) {
            String imgName = db.getNextImage() + house.getImageid();

            Path path = Paths.get(dir + imgName);
            try {
                Files.write(path, house.getImagebytes());
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            house.setImageid(imgName);
        }

        if (db.addHouse(house) == -1)
            return false;
        else
            return true;

    }

    @Override
    public boolean deleteHouse(House house) throws RemoteException {

        String name = house.getImageid();

        if (db.deleteHouse(house.getId()) == -1)
            return false;
        else{

            File fileDel = new File(dir + name);
            if(fileDel.exists())
                fileDel.delete();


            return true;

        }
    }

    @Override
    public List<House> getListings(int category, int location) throws RemoteException {
        List<House> list = new ArrayList<>();
        List<Integer> idList;


        idList = db.getListings(category, location);

        for (int id : idList){
            list.add(loadHouse(id));
        }

        return list;
    }

    @Override
    public List<House> getListingsbyAgent(int agentId) throws RemoteException {
        List<House> list = new ArrayList<>();
        List<Integer> idList;


        idList = db.getListingsbyAgent(agentId);

        for (int id : idList){
            list.add(loadHouse(id));
        }

        return list;
    }

    @Override
    public int login(String username, String password) throws RemoteException {
        return db.login(username, password);
    }

}
