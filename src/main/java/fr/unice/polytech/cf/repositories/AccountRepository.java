package fr.unice.polytech.cf.repositories;

import fr.unice.polytech.cf.entities.Account;
import fr.unice.polytech.cf.entities.Store;
import fr.unice.polytech.repositories.BasicRepositoryImpl;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class AccountRepository extends BasicRepositoryImpl<Account, UUID> {

}
