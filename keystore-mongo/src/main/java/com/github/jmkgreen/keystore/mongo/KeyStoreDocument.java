/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.jmkgreen.keystore.mongo;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author gaju
 */
@Document
public class KeyStoreDocument {

    private String id;
    private String keyStoreName;
    private byte[] keyStore;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKeyStoreName() {
        return keyStoreName;
    }

    public void setKeyStoreName(String keyStoreName) {
        this.keyStoreName = keyStoreName;
    }

    public byte[] getKeyStore() {
        return keyStore;
    }

    public void setKeyStore(byte[] keyStore) {
        this.keyStore = keyStore;
    }
}
