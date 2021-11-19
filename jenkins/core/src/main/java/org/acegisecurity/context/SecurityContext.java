/*
 * The MIT License
 *
 * Copyright 2020 CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.acegisecurity.context;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.model.User;
import hudson.security.ACL;
import org.acegisecurity.Authentication;

/**
 * @deprecated Use {@link ACL#as(User)} or {@link org.springframework.security.core.context.SecurityContext}
 */
@Deprecated
public interface SecurityContext {

    Authentication getAuthentication();

    void setAuthentication(Authentication a);

    static @NonNull SecurityContext fromSpring(@NonNull org.springframework.security.core.context.SecurityContext c) {
        return new SecurityContext() {
            @Override
            public Authentication getAuthentication() {
                org.springframework.security.core.Authentication a = c.getAuthentication();
                return a != null ? Authentication.fromSpring(a) : null;
            }
            @Override
            public void setAuthentication(Authentication a) {
                c.setAuthentication(a != null ? a.toSpring() : null);
            }
        };
    }

    default @NonNull org.springframework.security.core.context.SecurityContext toSpring() {
        return new org.springframework.security.core.context.SecurityContext() {
            @Override
            public org.springframework.security.core.Authentication getAuthentication() {
                Authentication a = SecurityContext.this.getAuthentication();
                return a != null ? a.toSpring() : null;
            }
            @Override
            public void setAuthentication(org.springframework.security.core.Authentication authentication) {
                SecurityContext.this.setAuthentication(authentication != null ? Authentication.fromSpring(authentication) : null);
            }
        };
    }

}
