
Vagrant.configure("2") do |config|

  config.vm.box = "ubuntu/jammy64"

  config.vm.network "forwarded_port", guest: 80, host: 8000
  config.vm.network "forwarded_port", guest: 8080, host: 8081
  
  config.vm.boot_timeout = 600

  config.vm.provider "virtualbox" do |vb|
    # vb.gui = true
    vb.gui = false
    # vb.memory = "2048"
    vb.memory = "4096"
    vb.cpus = 2
  end
  
end
