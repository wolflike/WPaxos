/*
 * Copyright (C) 2005-present, 58.com.  All rights reserved.
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
package com.wuba.wpaxos.store.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wuba.wpaxos.store.db.FileIndexDB;
import com.wuba.wpaxos.utils.ServiceThread;

/**
 * 一定会时间内不再访问的索引文件，取消内存映射
 */
public class CleanIndexMappedFileService extends ServiceThread {
	private static final Logger log = LogManager.getLogger(CleanIndexMappedFileService.class);
	private int groupId;
	private FileIndexDB fileIndexDB;

	public CleanIndexMappedFileService(int groupId, FileIndexDB fileIndexDB) {
		super(CleanIndexMappedFileService.class.getSimpleName() + "-" + groupId);
		this.fileIndexDB = fileIndexDB;
		this.groupId = groupId;
	}

	public void cleanUnMappedFiles() {
		fileIndexDB.getMapedFileQueue().cleanUnMappedFile();
	}

	@Override
	public void run() {
		try {
			this.cleanUnMappedFiles();
		} catch (Exception e) {
			log.warn(this.getServiceName() + " service has exception. ", e);
		}
	}

	@Override
	public String getServiceName() {
		return CleanIndexMappedFileService.class.getSimpleName() + "-" + groupId;
	}
}
