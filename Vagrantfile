Vagrant.configure(2) do |config|

  # Installed vagrant box - Ubuntu 14 (trusty)
  config.vm.box = "ubuntu/trusty64"

  # Disable automatic box update checking
  config.vm.box_check_update = false

  # Port mapping --------------------------------------------

  # PostgreSql
  config.vm.network "forwarded_port", guest: 5432, host: 5433

  # HTTP
  config.vm.network "forwarded_port", guest: 8080, host: 8080

  # ----------------------------------------------------------

  # Private network, host-only access to the machine
  config.vm.network "private_network", ip: "192.168.33.10"

  # Public network, the machine appear as another physical device on the network.
  config.vm.network "public_network", ip: "10.9.3.122"

  # Provider's (virtual box) settings
  config.vm.provider "virtualbox" do |vb|

  # Display the VirtualBox GUI when booting the machine
  vb.gui = false
  
  # Customize the amount of memory on the VM:
  vb.memory = "2048"
  end

  # Enable provisioning with a shell script
  config.vm.provision "shell", inline: <<-SHELL
 
  echo "--------------------> updating applications..."
  sudo apt-get update

  echo "--------------------> setting locales..."
  sudo echo “LANG=en_US.UTF-8” >> /etc/environment
  sudo echo “LANGUAGE=en_US.UTF-8” >> /etc/environment
  sudo echo “LC_ALL=en_US.UTF-8” >> /etc/environment
  sudo echo “LC_CTYPE=en_US.UTF-8” >> /etc/environment

  echo "--------------------> installing postgres..." 
  sudo vim /etc/apt/sources.list.d/postgresql.list
  deb http://apt.postgresql.org/pub/repos/apt/ xenial-pgdg main
  wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | \ sudo apt-key add -
  sudo apt-get install postgresql-9.3 -y 

  echo "--------------------> fixing listen_addresses on postgresql.conf"
  sudo sed -i "s/#listen_address.*/listen_addresses '*'/" /etc/postgresql/9.3/main/postgresql.conf

  # fix permissions
  echo "--------------------> fixing listen_addresses on postgresql.conf"
  sudo sed -i "s/#listen_address.*/listen_addresses '*'/" /etc/postgresql/9.3/main/postgresql.conf
  echo "--------------------> fixing postgres pg_hba.conf file"
  sudo cat >> /etc/postgresql/9.3/main/pg_hba.conf <<EOF
  # Accept all IPv4 connections - DEVELOPMENT ONLY
host    all         all         0.0.0.0/0             md5
EOF

  echo "--------------------> installing postgresql-client..."
  sudo apt-get install postgresql postgresql-client

  echo "--------------------> creating role(user) 'igor' with password '1' "
  sudo -u postgres psql -c "CREATE USER igor WITH
  LOGIN
  SUPERUSER
  CREATEDB
  CREATEROLE
  INHERIT
  REPLICATION
  CONNECTION LIMIT -1
  PASSWORD '1';"
  
  echo "--------------------> creating database 'testdb' with owner 'igor' "
  sudo -u postgres psql -c "CREATE DATABASE testdb OWNER igor;"

  echo "--------------------> creating schema 'users' in database 'testdb' "
  sudo -u postgres psql -d testdb -c "CREATE SCHEMA users;"

  echo "--------------------> creating table 'contacts' in database 'testdb', schema 'users' "
  sudo -u postgres psql -d testdb -c "CREATE TABLE users.contacts
  (
    id bigint NOT NULL,
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT contacts_pkey PRIMARY KEY (id)
  )
  WITH (
    OIDS = FALSE
  )
  TABLESPACE pg_default;
  ALTER TABLE users.contacts
    OWNER to igor;"

  echo "--------------------> inserting 5 values into the table 'contacts' "
  sudo -u postgres psql -d testdb -c "INSERT INTO users.contacts
  (id, name)
  VALUES
    (1, 'lion'),
    (2, 'rabbit'),
    (3, 'rat'),
    (4, 'dog'),
    (5, 'mouse');"

  echo "--------------------> restarting postgresql..."
  sudo service postgresql restart
  echo "--------------------> postgresql restarted!"

  echo "--------------------> installing JDK 8..."
  sudo apt-get install -y python-software-properties
  echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | sudo /usr/bin/debconf-set-selections
  sudo add-apt-repository -y ppa:webupd8team/java
  sudo apt-get update
  sudo apt-get install -y oracle-java8-installer

  echo "--------------------> setting environment variables for JDK 8..."
  sudo apt-get install -y oracle-java8-set-default

  echo "--------------------> installing Maven..."
  sudo apt-get install -y maven

  echo "--------------------> installing Git..."
  sudo apt-get install -y git

  echo "--------------------> cloning from repository 'hellobackend' project..."
  git clone https://github.com/remboo/hellobackend.git

  echo "--------------------> installing 'hellobackend' project"
  cd /home/vagrant/hellobackend
  mvn clean install

  sudo cp /home/vagrant/hellobackend/hellobackend.service /etc/systemd/system
  sudo systemctl daemon-reload
  sudo systemctl enable hellobackend
  sudo systemctl start hellobackend

  echo "--------------------> VM successfully launched!"
  SHELL
end
