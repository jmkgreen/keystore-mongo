/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.jmkgreen.keystore.mongo.repository;

import com.github.jmkgreen.keystore.mongo.KeyStoreDocument;
import com.sun.xml.bind.AnyTypeAdapter;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author jgreen
 */
@XmlJavaTypeAdapter(AnyTypeAdapter.class)
public interface KeyStoreRepository extends MongoRepository<KeyStoreDocument, String> {

    public void createOrUpdate(String name, char[] password, KeyStore store) throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException;

    public void save(String name, char[] password, KeyStore store) throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException;

    public KeyStore load(String name, String type, String password) throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException;

    public void setMongoTemplate(MongoTemplate mongoTemplate);

    public void setGridfsTemplate(GridFsTemplate gridfsTemplate);
}
