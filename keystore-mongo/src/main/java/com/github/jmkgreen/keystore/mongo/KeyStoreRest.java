/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.jmkgreen.keystore.mongo;

import com.github.jmkgreen.keystore.mongo.repository.KeyStoreRepository;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import org.apache.shiro.crypto.AesCipherService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author jgreen
 */
@Path("keystore")
public class KeyStoreRest {
    private static final Logger LOG = Logger.getLogger(KeyStoreRest.class.getName());

    @Autowired
    KeyStoreRepository keyStoreRepository;
    String storeName, storePassword;
    KeyStore store;

    public void setKeyStoreRepository(KeyStoreRepository keyStoreRepository) {
        this.keyStoreRepository = keyStoreRepository;
    }

    // Works
    @GET
    @Path("create-new-keystore")
    public void createNewKeyStore(@QueryParam("name") String name, @QueryParam("password") String password) throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException {
        KeyStore store = KeyStore.getInstance("JCEKS");
        store.load(null, null);
        keyStoreRepository.save(name, password.toCharArray(), store);
    }

    // Breaks
    @GET
    @Path("create-new-key")
    public void createNewKey(
            @QueryParam("keystoreName") String keystoreName,
            @QueryParam("keystorePassword") String password,
            @QueryParam("keyName") String keyName,
            @QueryParam("keyPassword") String keyPassword) throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException {
        KeyStore store = keyStoreRepository.load(keystoreName, "JCEKS", password);
        LOG.info("Creating a new key within a store currently holding " + store.size() + " keys.");
        AesCipherService cipherService = new AesCipherService();
        Key key = cipherService.generateNewKey(256);
        store.setKeyEntry(keyName, key.getEncoded(), null);
        keyStoreRepository.createOrUpdate(keystoreName, password.toCharArray(), store);
        LOG.info("Stored Key within a KeyStore currently holding " + store.size() + " keys.");
    }
}
