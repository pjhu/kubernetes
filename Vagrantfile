# coding: utf-8
# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure(2) do |config|
  if Vagrant.has_plugin?("vagrant-cachier")
    # Configure cached packages to be shared between instances of the same base box.
    # More info on http://fgrehm.viewdocs.io/vagrant-cachier/usage
    config.cache.scope = :box
  end

  cluster = {
    "master" => { :ip => "192.168.45.50", :mem => 1024,  :cpu => 1 },
    "node1" => { :ip => "192.168.45.51", :mem => 1024,  :cpu => 1 },
    "node2" => { :ip => "192.168.45.52", :mem => 1024,  :cpu => 1 },
    "single" => { :ip => "192.168.45.53", :mem => 1024,  :cpu => 1 },
  }

  cluster.each_with_index do | (hostname, info), index |
    config.vm.define hostname do | host |
      host.vm.box = "centos7"
      host.vm.provider "virtualbox" do |v|
        v.name   = "k8s-#{hostname}"
        v.memory = info[:mem]
        v.cpus   = info[:cpu]
      end
      host.vm.hostname = hostname
      host.vm.network "private_network", ip: info[:ip]
    end
  end
  config.vm.box_check_update = false
end
