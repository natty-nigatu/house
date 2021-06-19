package interfaces;

import models.Agent;
import models.House;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Server extends Remote {

    Agent loadAgent(int id) throws RemoteException;
    boolean saveAgent(Agent agent) throws RemoteException;

    House loadHouse(int id) throws RemoteException;
    boolean saveHouse(House house) throws RemoteException;
    boolean addHouse(House house) throws  RemoteException;
    boolean deleteHouse(House house) throws RemoteException;

    List<House> getListings(int category, int location) throws RemoteException;
    List<House> getListingsbyAgent(int agentId) throws RemoteException;

    int login(String username, String password) throws RemoteException;

}
