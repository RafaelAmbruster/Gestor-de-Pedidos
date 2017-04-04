
package com.app.ambruster.gestiondepedidos.data.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public abstract class AbstractIntegerIdentifier extends AbstractIdentifier
{

  @DatabaseField(id=true)
  public int id;

  public int getId()
  {
    return this.id;
  }

  public void setId(int paramInt)
  {
    this.id = paramInt;
  }
}

