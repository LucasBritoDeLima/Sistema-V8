SELECT os.*, c.*, 
r.idRetifica as retifica_id, r.nome_fantasia as retifica_nome_fantasia, r.razao_social as retifica_razao_social, r.cnpj as retifica_cnpj,
r.ie as retifica_ie, r.tel as retifca_tel, r.tel2 as retifca_tel2, r.tel3 as retifca_tel3, r.tel4 as retifca_tel4,
r.logradouro as retifica_logradouro, r.numero as retifica_numero, r.bairro as retifica_bairro, r.cep as retifica_cep, r.estado as retifica_estado,
r.cidade as retifica_cidade, r.foto as retifica_foto, 
m.* FROM tb_ordemdeservico os 
INNER JOIN tb_cliente c ON (os.cliente = c.idCliente) 
INNER JOIN tb_retifica r ON (r.idRetifica = os.retifica)
INNER JOIN tb_motor m ON (os.tb_motor_idMotor = m.idMotor)
WHERE os.id=5;