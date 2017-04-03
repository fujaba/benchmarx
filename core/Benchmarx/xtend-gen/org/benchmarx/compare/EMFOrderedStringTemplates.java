package org.benchmarx.compare;

import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class EMFOrderedStringTemplates {
  public String objToString(final String className, final List<String> mappings, final List<String> attributes) {
    if (((mappings.size() == 0) && (attributes.size() == 0))) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(className, "");
      _builder.append(" { empty|undefined }");
      _builder.newLineIfNotEmpty();
      return _builder.toString();
    } else {
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append(className, "");
      _builder_1.append(" {");
      _builder_1.newLineIfNotEmpty();
      {
        for(final String s : attributes) {
          _builder_1.append("\t");
          _builder_1.append(s, "\t");
          _builder_1.append(" ");
          _builder_1.newLineIfNotEmpty();
        }
      }
      {
        for(final String s_1 : mappings) {
          _builder_1.append("\t");
          _builder_1.append(s_1, "\t");
          _builder_1.append(" ");
          _builder_1.newLineIfNotEmpty();
        }
      }
      _builder_1.append("}");
      _builder_1.newLine();
      return _builder_1.toString();
    }
  }
  
  public String attributeToString(final String attribute, final String value) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(attribute, "");
    _builder.append(" = ");
    _builder.append(value, "");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  public String singleMappingToString(final String fieldName, final String entry) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(fieldName, "");
    _builder.append(" -> ");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append(entry, "\t");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  public String manyMappingToString(final String listName, final List<String> entries) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(listName, "");
    _builder.append(" = [");
    _builder.newLineIfNotEmpty();
    {
      boolean _hasElements = false;
      for(final String e : entries) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "\t");
        }
        _builder.append("\t");
        _builder.append(e, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("]");
    _builder.newLine();
    return _builder.toString();
  }
}
