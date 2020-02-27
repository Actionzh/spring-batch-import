/*
 * Copyright 2006-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.actionzh.batch.excel.mapping;

import com.actionzh.batch.excel.RowMapper;
import com.actionzh.batch.excel.rowset.RowSet;

/**
 * Pass through {@link RowMapper} useful for passing the orginal String[]
 * back directly rather than a mapped object.
 *
 * @author Marten Deinum
 * @since 0.5.0
 */
public class PassThroughRowMapper implements RowMapper<String[]> {

    @Override
    public String[] mapRow(final RowSet rs) throws Exception {
        return rs.getCurrentRow();
    }

}
