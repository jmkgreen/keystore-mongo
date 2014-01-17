/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.jmkgreen.keystore.mongo.repository;

import com.github.jmkgreen.keystore.mongo.KeyStoreDocument;
import com.mongodb.MongoException;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;

/**
 * Implementation based on MongoDB GridFS.
 *
 * @author jgreen
 */
@Repository
public class KeyStoreRepositoryImpl implements KeyStoreRepository {

    @Autowired
    GridFsTemplate gridfsTemplate;
    @Autowired
    MongoTemplate mongoTemplate;
    private static final Logger LOG = Logger.getLogger(KeyStoreRepositoryImpl.class.getName());
    private static final String NAME = "fs";

    @Override
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void setGridfsTemplate(GridFsTemplate gridfsTemplate) {
        this.gridfsTemplate = gridfsTemplate;
    }

    @Override
    public KeyStore load(String name, String type, String password) throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException {
        LOG.info("Loading KeyStore named " + name + " from database");
        KeyStore store = KeyStore.getInstance(type);
        GridFS fs = new GridFS(mongoTemplate.getDb(), NAME);
        GridFSDBFile file = fs.findOne(name);
        InputStream is = file.getInputStream();
        store.load(is, password.toCharArray());
        return store;
    }

    @Override
    public void createOrUpdate(String name, char[] password, KeyStore store) throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException {
        try {
            save(name, password, store);
        } catch (MongoException.DuplicateKey cause) {
            LOG.info("A KeyStore named " + name + " already exists. Removing then re-saving.");
            remove(name);
            save(name, password, store);
        }
    }

    @Override
    public void save(String name, char[] password, KeyStore keyStore) throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException {
        LOG.info("Saving KeyStore named " + name + " which has " + keyStore.size() + " entries");
        GridFS fs = new GridFS(mongoTemplate.getDb(), NAME);
        GridFSInputFile created = fs.createFile(name);
        created.setId(name);

        keyStore.store(created.getOutputStream(), password);
        created.getOutputStream().close();
        Enumeration<String> aliases = keyStore.aliases();
    }

    public void remove(String name) {
        GridFS fs = new GridFS(mongoTemplate.getDb(), NAME);
        fs.remove(name);
    }

    @Override
    public <S extends KeyStoreDocument> List<S> save(Iterable<S> entites) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<KeyStoreDocument> findAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<KeyStoreDocument> findAll(Sort sort) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Page<KeyStoreDocument> findAll(Pageable pageable) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <S extends KeyStoreDocument> S save(S entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public KeyStoreDocument findOne(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean exists(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iterable<KeyStoreDocument> findAll(Iterable<String> ids) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long count() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(KeyStoreDocument entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Iterable<? extends KeyStoreDocument> entities) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
