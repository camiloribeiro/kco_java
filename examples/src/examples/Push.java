/*
 * Copyright 2012 Klarna AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * File containing the Push example.
 */
package examples;

import com.klarna.checkout.Connector;
import com.klarna.checkout.IConnector;
import com.klarna.checkout.Order;
import java.io.IOException;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Push Example.
 */
public class Push extends BaseExample {

    /**
     * Run the example file. Called from the menu.
     *
     * @param session Session storage.
     *
     * @return session
     */
    @Override
    Map<String, Object> run(Map<String, Object> session) {
        this.session = session;
        if (this.session.containsKey("klarna_checkout")) {
            update();
        } else {
            System.out.println("No checkout in session.");
        }
        return this.session;
    }

    /**
     * Update a checkout resource.
     */
    private void update() {
        Order order = new Order();

        IConnector connector = null;
        try {
            connector = Connector.create("sharedSecret");
        } catch (NoSuchAlgorithmException ex) {
            System.err.println("SHA-256 digest not supported.");
            System.exit(0);
        }

        try {
            order.fetch(connector, (URI) session.get("klarna_checkout"));
        } catch (IOException ex) {
            Logger.getLogger(
                    Checkout.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (order.get("status").equals("checkout_complete")) {
            order.set("status", "created");
            order.set("merchant_reference", new HashMap<String, Object>() {
                {
                    put("orderid1", "CheckoutExample");
                }
            });
            try {
                order.update(connector);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }
}
