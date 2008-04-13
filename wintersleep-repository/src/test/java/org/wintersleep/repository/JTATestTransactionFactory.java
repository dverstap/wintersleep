/*
 * Copyright 2008 Davy Verstappen.
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
 */

package org.wintersleep.repository;

import com.arjuna.ats.jta.UserTransaction;
import org.hibernate.HibernateException;
import org.hibernate.transaction.JTATransactionFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class JTATestTransactionFactory extends JTATransactionFactory {

    @Override
    public void configure(Properties props) throws HibernateException {
        super.configure(props);
        try {
            this.context = new InitialContext(props) {
                @Override
                public Object lookup(String name) throws NamingException {
                    if ("java:comp/UserTransaction".equals(name)) {
                        return UserTransaction.userTransaction();
                    } else {
                        return super.lookup(name);
                    }
                }

            };
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }

    }
}
