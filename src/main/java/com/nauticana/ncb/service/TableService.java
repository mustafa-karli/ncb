package com.nauticana.ncb.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nauticana.ncb.model.FieldType;
import com.nauticana.ncb.model.TableDefinition;
import com.nauticana.ncb.model.TableRelation;
import com.nauticana.ncb.model.Utils;
import com.nauticana.ncb.repository.BasisRepository;

@Service
public class TableService {

    @Autowired
    protected BasisRepository					basisRepository;

	//	public static final HashMap<String, FieldType[]> fieldTypes = new HashMap<String, FieldType[]>(0);
	public static final HashMap<String, TableDefinition> tableDefinitions = new HashMap<String, TableDefinition>(0);
	public static final HashMap<String, TableRelation> relations = new HashMap<String, TableRelation>(0);

	public void loadAllTables() throws SQLException {
		if (tableDefinitions.isEmpty()) {
			for (String tableName : basisRepository.allTables()) {
				getTableDefinition(tableName);
			}
		}
	}
	
	public TableDefinition getTableDefinition(String tableName) throws SQLException {
		if (Utils.emptyStr(tableName)) return null;
		TableDefinition tableDefinition = tableDefinitions.get(tableName);
		if (tableDefinition != null) return tableDefinition;
		tableDefinition = basisRepository.retreiveTableDefinition(tableName);
		basisRepository.setTableFields(tableDefinition);
		tableDefinition.setContentTypes(basisRepository.retreiveContentTypes(tableName));
		tableDefinition.setActions(basisRepository.retreiveTableActions(tableName));
		tableDefinitions.put(tableName, tableDefinition);
		loadForeignKeys(tableDefinition);
		return tableDefinition;
	}

	public void loadForeignKeys(TableDefinition tableDefinition) throws SQLException {
		ArrayList<String> parents = new ArrayList<String>();
		ArrayList<String> forkeys = new ArrayList<String>();
		ArrayList<ArrayList<String>> pKeys = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> cKeys = new ArrayList<ArrayList<String>>();
		
		basisRepository.retreiveParentForeignKeys(tableDefinition, parents, forkeys, pKeys, cKeys);
		
		for (int i = 0; i < parents.size(); i++) {
			String foreignKey = forkeys.get(i);
			TableRelation relation = relations.get(foreignKey);
			TableDefinition parent = getTableDefinition(parents.get(i));
			if (relation == null & parent != null) {
				ArrayList<String> pKey = pKeys.get(i);
				ArrayList<String> cKey = cKeys.get(i);
				FieldType[] parentKeys = new FieldType[pKey.size()];
				FieldType[] childKeys = new FieldType[cKey.size()];
				for (int j = 0; j < pKey.size(); j++) {
					parentKeys[j] = parent.getField(pKey.get(j));
					childKeys[j] = tableDefinition.getField(cKey.get(j));
				}
				relation = new TableRelation(foreignKey, parent, tableDefinition, parentKeys, childKeys);
				relations.put(foreignKey, relation);
				parent.addChild(relation);
				tableDefinition.addParent(relation);
			}
		}
		
	}
	
	
	public TableRelation getRelation(String fkname) {
		return relations.get(fkname);
	}

	public String typeScriptClass(String baseClass, String tableName) throws SQLException {
		TableDefinition t = getTableDefinition(tableName);
		if (t == null) return null;
		return t.typeScriptClass(baseClass);
	}

	public List<String> allTypeScriptClasses(String baseClass, List<String> moduleNames, List<String> classNames) throws SQLException {
		List<String[]> records = basisRepository.retreiveModuleTables();
		ArrayList<String> classes = new ArrayList<String>();
		for (String[] record : records) {
			String s = typeScriptClass(baseClass, record[0]);
			if (s != null) {
				classes.add(s);
				classNames.add(Utils.titleCase(record[0]));
				moduleNames.add(record[1]);
			}
		}
		return classes;
	}
	
	
	public List<String> allRelations() {
		Set<String> keys = relations.keySet();
		ArrayList<String> list = new ArrayList<String>();
		for(String key : keys) {
			TableRelation relation = relations.get(key);
			list.add(relation.relationName + ","+relation.parent.getTableName()+","+relation.child.getTableName());
		}
		return list;
	}
	
}
