package interfaces;

import models.Agent;
import models.House;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Database extends Remote {

    String getNextImage() throws RemoteException;
    List<Integer> getListings(int category, int location) throws RemoteException;
    List<Integer> getListingsbyAgent(int agentId) throws RemoteException;
    int login(String username, String password) throws RemoteException;
    House getListingDetail(int id) throws RemoteException;
    Agent getAgentDetail(int id) throws RemoteException;
    int saveAgent(Agent agent) throws RemoteException;
    int saveHouse(House house) throws RemoteException;
    int addHouse(House house) throws RemoteException;
    int deleteHouse(int id) throws RemoteException;

}
